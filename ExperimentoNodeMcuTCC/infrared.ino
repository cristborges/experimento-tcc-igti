#include <IRremoteESP8266.h>
#include <IRsend.h>

IRsend irsend(INFRARED_PIN);

void initIRSend() {
  irsend.begin();

  airConditionerOn = false;
  airConditionerTemperature = 0.0;
  remoteAirConditionerOn = false;
  remoteAirConditionerTemperature = 0.0;
  airConditionerSetStatusMsgReceived = false;
  airConditionerRemoteCommand = (uint16_t*) malloc(sizeof(uint16_t) * AIR_CONDITIONER_REMOTE_COMMAND_MAX_LEN);

  updateAirConditionerStatus();
}

void updateAirConditionerStatus() {
  if (airConditionerSetStatusMsgReceived) {
    airConditionerSetStatusMsgReceived = false;

    irsend.sendRaw(airConditionerRemoteCommand, AIR_CONDITIONER_REMOTE_COMMAND_MAX_LEN, INFRARED_FREQ_KHZ);
    airConditionerOn = remoteAirConditionerOn;
    airConditionerTemperature = remoteAirConditionerTemperature;
  } else if (
    numSchedulesExecuted < SCHEDULES_LEN && 
    schedulesUnixTime[numSchedulesExecuted][0] != 0UL &&
    schedulesUnixTime[numSchedulesExecuted][1] != 0UL
  ) {
    byte numSchedulesExecutedOld = numSchedulesExecuted;
    boolean schedulingRunningOld = schedulingRunning;

    while (
      (numSchedulesExecuted + 1 < SCHEDULES_LEN || !schedulingRunning) && 
      schedulesUnixTime[numSchedulesExecuted + (schedulingRunning ? 1 : 0)][0] != 0UL && 
      schedulesUnixTime[numSchedulesExecuted + (schedulingRunning ? 1 : 0)][1] != 0UL && 
      currentUnixTime > schedulesUnixTime[numSchedulesExecuted + (schedulingRunning ? 1 : 0)][schedulingRunning ? 0 : 1]
    ) {
      numSchedulesExecuted += (byte) (schedulingRunning ? 1 : 0);
      schedulingRunning = !schedulingRunning;
    }

    if (currentUnixTime > schedulesUnixTime[numSchedulesExecuted][schedulingRunning ? 1 : 0]) {
      if (schedulingRunning) {
        scheduleTurnOffAirConditioner();
      } else {
        scheduleTurnOnAirConditioner();
      }

      numSchedulesExecuted += (byte) (schedulingRunning ? 1 : 0);
      schedulingRunning = !schedulingRunning;
    }

    if (numSchedulesExecuted != numSchedulesExecutedOld || schedulingRunning != schedulingRunningOld) {
      writeScheduling();
    }
  }
}

void scheduleTurnOnAirConditioner() {
  irsend.sendRaw(scheduleAirConditionerOnCommand, SCHEDULE_AIR_CONDITIONER_ON_COMMAND_LEN, INFRARED_FREQ_KHZ);
  airConditionerOn = true;

  if (isnan(scheduleAirConditionerTemperature)) {
    airConditionerTemperature = 0.0;
  } else {
    airConditionerTemperature = scheduleAirConditionerTemperature;
  }
}

void scheduleTurnOffAirConditioner() {
  irsend.sendRaw(scheduleAirConditionerOffCommand, SCHEDULE_AIR_CONDITIONER_OFF_COMMAND_LEN, INFRARED_FREQ_KHZ);
  airConditionerOn = false;
}

