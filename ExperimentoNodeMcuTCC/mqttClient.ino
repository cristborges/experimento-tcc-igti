#include <PubSubClient.h>
#include <ArduinoJson.h>

PubSubClient MQTT;

static char* mqttClientId;
static unsigned long mqttLastReconnectAttempt;

boolean wiFiConnected();

void callbackMQTT(const char* topic, byte* payload, unsigned int length);

void initMQTT() {
  delay(10);

  MQTT.setServer(mqttAddress, mqttPort);
  MQTT.setCallback(callbackMQTT);

  mqttClientId = (char*) malloc(strlen(MQTT_ID_PREFIX) + CLASSROOM_ID_LEN);
  strcpy(mqttClientId, MQTT_ID_PREFIX);
  strcat(mqttClientId, classroomID);

  mqttLastReconnectAttempt = 0UL;

  connectMQTT();
}

boolean connectMQTT() {
  if (wiFiConnected()) {
    Serial.printf("Tentando se conectar ao MQTT: %s.\r\n", mqttAddress);

    if (MQTT.connect(mqttClientId)) {
      Serial.println("Conectado ao MQTT!");

      MQTT.subscribe(CLASSROOM_COURSE_SCHEDULING_SET_TOPIC);
      MQTT.subscribe(CLASSROOM_COURSE_SCHEDULING_IR_COMMANDS_TOPIC);
      MQTT.subscribe(CLASSROOM_AIR_CONDITIONER_IR_COMMAND_TOPIC);
    }
  }

  return mqttConnected();
}

boolean verifyMQTTConnection() {
  if (!mqttConnected()) {
    long now = millis();

    if (now - mqttLastReconnectAttempt > 5000) {
      mqttLastReconnectAttempt = now;

      if (connectMQTT()) {
        mqttLastReconnectAttempt = 0UL;
      }
    }
  }

  return mqttConnected();
}

boolean mqttConnected() {
  return MQTT.connected();
}

void keepAliveMQTT() {
  MQTT.loop();
}

void callbackMQTT(const char* topic, byte* payload, unsigned int length) {
  DynamicJsonBuffer jsonBuffer(MQTT_RECEIVE_MESSAGE_MAX_LEN);
  JsonObject& jsonObject = jsonBuffer.parseObject((char*) payload);

  if (!jsonObject.success()) {
    Serial.printf("Falhou ao obter o JSON %s.\r\n", (char*) payload);
    return;
  }

  if (strcmp(topic, CLASSROOM_COURSE_SCHEDULING_SET_TOPIC) == 0) {
    if (strcmp(classroomID, jsonObject["sala"]) == 0) {
      strcpy(scheduledCourse, (char*) jsonObject["curso"].as<char*>());
      strcpy(scheduledClass, (char*) jsonObject["turma"].as<char*>());

      JsonArray& jsonArraySchedules = jsonObject["agendamentos"];
      size_t schedulesSize = jsonArraySchedules.size();

      for (size_t i = 0; i < schedulesSize; i++) {
        for (size_t j = 0; j < 2; j++) {
          schedulesUnixTime[i][j] = jsonArraySchedules[i][j];
        }
      }

      numSchedulesExecuted = 0;
      schedulingRunning = false;

      writeScheduling();
    }
  } else if (strcmp(topic, CLASSROOM_COURSE_SCHEDULING_IR_COMMANDS_TOPIC) == 0) {
    if (strcmp(classroomID, jsonObject["sala"]) == 0) {
      scheduleAirConditionerTemperature = jsonObject["temperatura"];

      JsonArray& jsonArrayScheduleAirConditionerOnCommand = jsonObject["comandoLigar"];
      size_t scheduleAirConditionerOnCommandSize = jsonArrayScheduleAirConditionerOnCommand.size();

      for (size_t i = 0; i < SCHEDULE_AIR_CONDITIONER_ON_COMMAND_LEN; i++) {
        scheduleAirConditionerOnCommand[i] = i < scheduleAirConditionerOnCommandSize ? jsonArrayScheduleAirConditionerOnCommand[i] : 0U;
      }

      JsonArray& jsonArrayScheduleAirConditionerOffCommand = jsonObject["comandoDesligar"];
      size_t scheduleAirConditionerOffCommandSize = jsonArrayScheduleAirConditionerOffCommand.size();

      for (size_t i = 0; i < SCHEDULE_AIR_CONDITIONER_OFF_COMMAND_LEN; i++) {
        scheduleAirConditionerOffCommand[i] = i < scheduleAirConditionerOffCommandSize ? jsonArrayScheduleAirConditionerOffCommand[i] : 0U;
      }

      writeSchedulingInfraredCommands();
    }
  } else if (strcmp(topic, CLASSROOM_AIR_CONDITIONER_IR_COMMAND_TOPIC) == 0) {
    if (strcmp(classroomID, jsonObject["sala"]) == 0) {
      airConditionerSetStatusMsgReceived = true;

      JsonArray& jsonArrayCommand = jsonObject["comando"];
      size_t commandSize = jsonArrayCommand.size();

      for (size_t i = 0; i < AIR_CONDITIONER_REMOTE_COMMAND_MAX_LEN; i++) {
        airConditionerRemoteCommand[i] = i < commandSize ? jsonArrayCommand[i] : 0U;
      }

      remoteAirConditionerTemperature = jsonObject["temperatura"];
      remoteAirConditionerOn = remoteAirConditionerTemperature > 0.0;

      if (!remoteAirConditionerOn) {
        remoteAirConditionerTemperature = airConditionerTemperature;
      }
    }
  }
}

void publishClassroomInfoToMQTT() {
  DynamicJsonBuffer jsonBuffer(MQTT_SEND_MESSAGE_MAX_LEN);
  JsonObject& jsonObject = jsonBuffer.createObject();

  jsonObject["sala"] = classroomID;
  jsonObject["temperatura"] = temperature;
  jsonObject["umidade"] = humidity;
  jsonObject["arCondicionadoLigado"] = airConditionerOn;
  jsonObject["temperaturaArCondicionado"] = airConditionerTemperature;

  if (numSchedulesExecuted < SCHEDULES_LEN && schedulingRunning) {
    jsonObject["curso"] = scheduledCourse;
    jsonObject["turma"] = scheduledClass;
  }

  char payload[MQTT_SEND_MESSAGE_MAX_LEN];
  jsonObject.printTo(payload, MQTT_SEND_MESSAGE_MAX_LEN);

  MQTT.publish(CLASSROOM_INFO_TOPIC, payload);

  Serial.println(payload);
}

