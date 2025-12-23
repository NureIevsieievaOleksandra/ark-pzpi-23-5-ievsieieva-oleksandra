mod rgb_led;
mod stats;
mod wifi;

use stats::energy_tracker::*;
use stats::statistics::*;

use anyhow::Result;
use esp_idf_hal::delay::FreeRtos;
use esp_idf_svc::eventloop::EspSystemEventLoop;
use esp_idf_svc::nvs::EspDefaultNvsPartition;
use esp_idf_svc::wifi::{BlockingWifi, EspWifi};
use esp_idf_sys as _;
use log::*;
use serde::Deserialize;
use serde_json::from_str;
use std::time::{Duration, Instant};
use tungstenite::{connect, Message, Utf8Bytes};
use url::Url;

use crate::rgb_led::RgbLed;
use crate::wifi::connect_wifi;

#[derive(Deserialize, Debug)]
pub struct RgbCommand {
    pub r: u8,
    pub g: u8,
    pub b: u8,
    pub brightness: u8,
}

use crate::stats::energy_tracker::EnergyTracker;
use crate::stats::statistics::{last_reset_reason, uptime_ms};
use esp_idf_hal::{peripherals::Peripherals, rmt::RmtChannel};
use ws2812_esp32_rmt_driver::Ws2812Esp32Rmt;


const id:u32 = 1;
fn main() -> anyhow::Result<()> {
    esp_idf_sys::link_patches();
    esp_idf_svc::log::EspLogger::initialize_default();

    info!("Starting ESP32-S3 RGB WebSocket Controller");

    let peripherals = Peripherals::take()?;
    let sys_loop = EspSystemEventLoop::take()?;
    let nvs = EspDefaultNvsPartition::take()?;

    // Налаштування Wi-Fi
    let mut wifi = BlockingWifi::wrap(
        EspWifi::new(peripherals.modem, sys_loop.clone(), Some(nvs))?,
        sys_loop,
    )?;

    connect_wifi(&mut wifi)?;

    // Налаштування RGB LED з трьома окремими таймерами
    let mut rgb_led = RgbLed::new(
        peripherals.ledc.timer0,
        peripherals.ledc.channel0,
        peripherals.ledc.channel1,
        peripherals.ledc.channel2,
        peripherals.pins.gpio38,
        peripherals.pins.gpio39,
        peripherals.pins.gpio40,
    )?;

    let mut energy_tracker = EnergyTracker::new(uptime_ms());
    info!("Energy Tracker: {energy_tracker:?}");

    init_temperature_sensor();

    let (mut socket, response) = connect("ws://192.168.50.200:8080/ws")?; // <-- передаємо рядок
    info!("Connected, response code: {}", response.status());

    let mut last_report = Instant::now();
    let mut brightness = 0;
    loop {
        energy_tracker.update(uptime_ms(), brightness, brightness > 0);
        let msg = socket.read_message()?;
        match msg {
            Message::Text(txt) => {
                if let Ok(cmd) = from_str::<RgbCommand>(&txt) {
                    info!("Received command: {:?}", cmd);
                    brightness = cmd.brightness;
                    rgb_led.set_color(cmd.r, cmd.g, cmd.b, cmd.brightness)?;
                }
            }
            Message::Binary(_) => {}
            Message::Ping(p) => socket.write_message(Message::Pong(p))?,
            Message::Pong(_) => {}
            Message::Close(_) => {}
            Message::Frame(_) => {
                info!("Received frame");
            }
        }

        if last_report.elapsed() >= Duration::from_secs(10) {
            last_report = Instant::now();

            let uptime = uptime_ms();
            info!("Uptime: {uptime} ms");
            let last_reset_reason = last_reset_reason();
            info!("Last reset reason: {last_reset_reason:?}");
            let temperature = temperature_celsius();
            match temperature {
                None => {}
                Some(value) => {
                    info!("Temperature: {value:?}");
                }
            }

            let stats_json = serde_json::json!({
                "id": id,
                "uptime": uptime,
                "last_reset": format!("{:?}", last_reset_reason),
                "energy": energy_tracker,
                "temperature": temperature,
            });

            if let Err(e) =
                socket.write_message(Message::Text(Utf8Bytes::from(stats_json.to_string())))
            {
                error!("Failed to send stats: {:?}", e);
            } else {
                info!("Sent statistics: {:?}", stats_json);
            }
        }
        {}
    }
}
