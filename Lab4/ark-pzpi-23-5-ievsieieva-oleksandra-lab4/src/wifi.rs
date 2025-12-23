
use esp_idf_svc::wifi::{BlockingWifi, EspWifi};
use anyhow::Result;
use embedded_svc::wifi::{ClientConfiguration, Configuration};
use log::*;
const SSID: &str = env!("WIFI_SSID");
const PASS: &str = env!("WIFI_PASS");
// use crate::{PASS, SSID};

pub fn connect_wifi(wifi: &mut BlockingWifi<EspWifi<'static>>) -> Result<()> {
    let wifi_configuration = Configuration::Client(ClientConfiguration {
        ssid: SSID.try_into().unwrap(),
        password: PASS.try_into().unwrap(),
        ..Default::default()
    });

    wifi.set_configuration(&wifi_configuration)?;
    wifi.start()?;
    info!("Wifi started");

    wifi.connect()?;
    info!("Wifi connected");

    wifi.wait_netif_up()?;
    info!("Wifi netif up");

    let ip_info = wifi.wifi().sta_netif().get_ip_info()?;
    info!("IP info: {:?}", ip_info);

    Ok(())
}