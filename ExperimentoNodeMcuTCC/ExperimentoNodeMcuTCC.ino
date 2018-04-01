#include <Arduino.h>
#include <math.h>
#include "config.h"

static char* wiFiSSID;
static char* wiFiPassword;
static char* mqttAddress;
static int mqttPort;
static char* classroomID;
static char* scheduledCourse;
static char* scheduledClass;
static unsigned long schedulesUnixTime[SCHEDULES_LEN][2];
static byte numSchedulesExecuted;
static boolean schedulingRunning;
static uint16_t scheduleAirConditionerOnCommand[SCHEDULE_AIR_CONDITIONER_ON_COMMAND_LEN];
static uint16_t scheduleAirConditionerOffCommand[SCHEDULE_AIR_CONDITIONER_OFF_COMMAND_LEN];
static float scheduleAirConditionerTemperature;

float temperature;
float humidity;
boolean airConditionerOn;
boolean remoteAirConditionerOn;
float airConditionerTemperature;
float remoteAirConditionerTemperature;
uint16_t* airConditionerRemoteCommand;
boolean airConditionerSetStatusMsgReceived;
unsigned long currentUnixTime;

void initSerial();
void initEEPROM();
void initWiFi();
void initMQTT();
void initDHT();
void initIRSend();

void setup() {
  initSerial();

  delay(2000);

  initEEPROM();
  initWiFi();
  initMQTT();
  initDHT();
  initIRSend();
}

void loop() {
  readDHT();

  updateAirConditionerStatus();

  if (verifyWiFiConnection()) {
    if (verifyMQTTConnection()) {
      publishClassroomInfoToMQTT();
      keepAliveMQTT();
    }
  }

  delay(1000);
}

