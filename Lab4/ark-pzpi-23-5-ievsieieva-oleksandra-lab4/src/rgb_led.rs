use anyhow::Result;
use esp_idf_hal::ledc::{config::TimerConfig, LedcDriver, LedcTimerDriver};
use esp_idf_hal::peripheral::Peripheral;
use esp_idf_hal::prelude::*;

pub struct RgbLed<'a> {
    red: LedcDriver<'a>,
    green: LedcDriver<'a>,
    blue: LedcDriver<'a>,
}

impl<'a> RgbLed<'a> {
    pub fn new<T, C0, C1, C2>(
        timer_r: impl Peripheral<P = T> + 'a,
        channel_r: impl Peripheral<P = C0> + 'a,
        channel_g: impl Peripheral<P = C1> + 'a,
        channel_b: impl Peripheral<P = C2> + 'a,
        pin_r: impl Peripheral<P = impl esp_idf_hal::gpio::OutputPin> + 'a,
        pin_g: impl Peripheral<P = impl esp_idf_hal::gpio::OutputPin> + 'a,
        pin_b: impl Peripheral<P = impl esp_idf_hal::gpio::OutputPin> + 'a,
    ) -> Result<Self>
    where
        T: esp_idf_hal::ledc::LedcTimer + 'a,
        C0: esp_idf_hal::ledc::LedcChannel<SpeedMode = T::SpeedMode> + 'a,
        C1: esp_idf_hal::ledc::LedcChannel<SpeedMode = T::SpeedMode> + 'a,
        C2: esp_idf_hal::ledc::LedcChannel<SpeedMode = T::SpeedMode> + 'a,
    {
        let config = TimerConfig::default().frequency(25.kHz().into());
        let timer = LedcTimerDriver::new(timer_r, &config)?;

        let red = LedcDriver::new(channel_r, &timer, pin_r)?;
        let green = LedcDriver::new(channel_g, &timer, pin_g)?;
        let blue = LedcDriver::new(channel_b, &timer, pin_b)?;

        Ok(Self { red, green, blue })
    }

    pub fn set_color(&mut self, r: u8, g: u8, b: u8, brightness: u8) -> Result<()> {
        let brightness_factor = brightness as u32 * 100 / 255;
        let max_duty = self.red.get_max_duty();

        let red_duty = (r as u32 * max_duty * brightness_factor) / (255 * 100);
        let green_duty = (g as u32 * max_duty * brightness_factor) / (255 * 100);
        let blue_duty = (b as u32 * max_duty * brightness_factor) / (255 * 100);

        self.red.set_duty(red_duty)?;
        self.green.set_duty(green_duty)?;
        self.blue.set_duty(blue_duty)?;

        Ok(())
    }
}

