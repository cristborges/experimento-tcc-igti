#include <ESP8266WiFi.h>
#include <EasyNTPClient.h>
#include <WiFiUdp.h>

WiFiClient wiFiClient;
WiFiUDP udp;
IPAddress unspecifiedIPAddress(0, 0, 0, 0);
EasyNTPClient ntpClient(udp, "time.nist.gov");

static unsigned long wiFiLastReconnectAttempt;

void initWiFi() {
  delay(10);

  MQTT.setClient(wiFiClient);

  wiFiLastReconnectAttempt = 0UL;
  currentUnixTime = 0UL;

  connectWiFi();
}

boolean connectWiFi() {
  Serial.printf("Tentando se conectar ao Wifi %s.\r\n", wiFiSSID);

  if (WiFi.begin(wiFiSSID, wiFiPassword) == WL_CONNECTED) {
    uint8_t mac[6];
    WiFi.macAddress(mac);
    Serial.printf("Dispositivo com o endereço MAC %02x:%02x:%02x:%02x:%02x:%02x conectado ao WiFi %s.\r\n", mac[0], mac[1], mac[2], mac[3], mac[4], mac[5], wiFiSSID);
  }

  return wiFiConnected();
}

boolean verifyWiFiConnection() {
  if (!wiFiConnected()) {
    long now = millis();

    if (now - wiFiLastReconnectAttempt > 5000) {
      wiFiLastReconnectAttempt = now;

      if (connectWiFi()) {
        wiFiLastReconnectAttempt = 0UL;
      }
    }
  }

  if (wiFiConnected()) {
    currentUnixTime = ntpClient.getUnixTime();
  }

  return wiFiConnected();
}

boolean wiFiConnected() {
  return WiFi.localIP() != unspecifiedIPAddress;
}

