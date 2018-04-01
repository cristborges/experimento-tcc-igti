#include <EEPROM.h>

#define EEPROM_END 0
#define EEPROM_START 1

int wiFiSSIDAddr = EEPROM_CONFIG_PARAMS_INITIAL_ADDR;
int wiFiPasswordAddr = wiFiSSIDAddr + WI_FI_SSID_LEN + 2;
int mqttAddressAddr = wiFiPasswordAddr + WI_FI_PASSWORD_LEN + 2;
int mqttPortAddr = mqttAddressAddr + MQTT_ADDRESS_LEN + 2;
int classroomIDAddr = mqttPortAddr + MQTT_PORT_LEN + 1;

int scheduledCourseAddr = EEPROM_SCHEDULING_INITIAL_ADDR;
int scheduledClassAddr = scheduledCourseAddr + SCHEDULED_COURSE_LEN + 2;
int schedulesExecutionInfoAddr = scheduledClassAddr + SCHEDULED_CLASS_LEN + 2;
int schedulesUnixTimeAddr = schedulesExecutionInfoAddr + SCHEDULES_EXECUTION_INFO_LEN + 1;

int scheduleAirConditionerOnCommandAddr = EEPROM_SCHEDULING_INFRARED_COMMANDS_INITIAL_ADDR;
int scheduleAirConditionerOffCommandAddr = scheduleAirConditionerOnCommandAddr + (SCHEDULE_AIR_CONDITIONER_ON_COMMAND_LEN * 2) + 1;
int scheduleAirConditionerTemperatureAddr = scheduleAirConditionerOffCommandAddr + (SCHEDULE_AIR_CONDITIONER_OFF_COMMAND_LEN * 2) + 1;

void initEEPROM() {
  wiFiSSID = (char*) malloc(WI_FI_SSID_LEN);
  char* wiFiSSIDOld = (char*) malloc(WI_FI_SSID_LEN);
  wiFiPassword = (char*) malloc(WI_FI_PASSWORD_LEN);
  char* wiFiPasswordOld = (char*) malloc(WI_FI_PASSWORD_LEN);
  mqttAddress = (char*) malloc(MQTT_ADDRESS_LEN);
  char* mqttAddressOld = (char*) malloc(MQTT_ADDRESS_LEN);
  mqttPort = 0;
  int mqttPortOld = 0;
  classroomID = (char*) malloc(CLASSROOM_ID_LEN);
  char* classroomIDOld = (char*) malloc(CLASSROOM_ID_LEN);

  int wiFiSSIDLength = EEPROMReadCharArray(wiFiSSIDAddr, wiFiSSID);
  strcpy(wiFiSSIDOld, wiFiSSID);
  int wiFiPasswordLength = EEPROMReadCharArray(wiFiPasswordAddr, wiFiPassword);
  strcpy(wiFiPasswordOld, wiFiPassword);
  int mqttAddressLength = EEPROMReadCharArray(mqttAddressAddr, mqttAddress);
  strcpy(mqttAddressOld, mqttAddress);
  mqttPort = 0;
  EEPROMReadAnything(mqttPortAddr, mqttPort, true);
  mqttPortOld = mqttPort;
  int classroomIDLength = EEPROMReadCharArray(classroomIDAddr, classroomID);
  strcpy(classroomIDOld, classroomID);

  scheduledCourse = (char*) malloc(SCHEDULED_COURSE_LEN);
  scheduledClass = (char*) malloc(SCHEDULED_CLASS_LEN);

  EEPROMReadCharArray(scheduledCourseAddr, scheduledCourse);
  EEPROMReadCharArray(scheduledClassAddr, scheduledClass);

  byte schedulesExecutionInfo = 0;
  EEPROMReadAnything(schedulesExecutionInfoAddr, schedulesExecutionInfo, true);
  if (isnan(schedulesExecutionInfo)) {
    numSchedulesExecuted = 0;
    schedulingRunning = 0;
  } else {
    numSchedulesExecuted = (byte) (schedulesExecutionInfo / 2);
    schedulingRunning = schedulesExecutionInfo % 2 == 1;
  }

  EEPROMReadTwoDimensionalUnsignedLongArray(schedulesUnixTimeAddr, SCHEDULES_LEN, 2, (unsigned long*) schedulesUnixTime);

  EEPROMReadUnsignedInt16Array(scheduleAirConditionerOnCommandAddr, SCHEDULE_AIR_CONDITIONER_ON_COMMAND_LEN, scheduleAirConditionerOnCommand);
  EEPROMReadUnsignedInt16Array(scheduleAirConditionerOffCommandAddr, SCHEDULE_AIR_CONDITIONER_OFF_COMMAND_LEN, scheduleAirConditionerOffCommand);

  scheduleAirConditionerTemperature = 0.0;
  EEPROMReadAnything(scheduleAirConditionerTemperatureAddr, scheduleAirConditionerTemperature, true);

  if (wiFiSSIDLength > 0 && wiFiPasswordLength > 0 && mqttAddressLength > 0 && mqttPort > 0 && classroomIDLength > 0 && !needEraseEEPROM()) {
      return;
  }

  readFromSerial("> SSID do Wi-Fi: ", wiFiSSID, wiFiSSIDOld, WI_FI_SSID_LEN, 0, false);
  EEPROMWriteCharArray(wiFiSSIDAddr, wiFiSSID, strlen(wiFiSSID));

  readFromSerial("> Senha do Wi-Fi: ", wiFiPassword, wiFiPasswordOld, WI_FI_PASSWORD_LEN, 0, true);
  EEPROMWriteCharArray(wiFiPasswordAddr, wiFiPassword, strlen(wiFiPassword));

  readFromSerial("> Endereço do MQTT: ", mqttAddress, mqttAddressOld, MQTT_ADDRESS_LEN, 0, false);
  EEPROMWriteCharArray(mqttAddressAddr, mqttAddress, strlen(mqttAddress));

  char mqttPortStr[MQTT_PORT_CHAR_ARRAY_LEN];
  sprintf(mqttPortStr, MQTT_PORT_CHAR_ARRAY_FORMAT, mqttPort);
  char mqttPortOldStr[MQTT_PORT_CHAR_ARRAY_LEN];
  sprintf(mqttPortOldStr, MQTT_PORT_CHAR_ARRAY_FORMAT, mqttPortOld);
  readFromSerial("> Porta do MQTT: ", mqttPortStr, mqttPortOldStr, MQTT_PORT_CHAR_ARRAY_LEN, 0, false);
  mqttPort = atoi(mqttPortStr);
  EEPROMWriteAnything(mqttPortAddr, mqttPort, true);

  readFromSerial("> Número da sala de aula: ", classroomID, classroomIDOld, CLASSROOM_ID_LEN, 0, false);
  EEPROMWriteCharArray(classroomIDAddr, classroomID, strlen(classroomID));
}

void writeScheduling() {
  EEPROMClear(EEPROM_SCHEDULING_INITIAL_ADDR, EEPROM_SIZE_SCHEDULING);

  EEPROMWriteCharArray(scheduledCourseAddr, scheduledCourse, strlen(scheduledCourse));
  EEPROMWriteCharArray(scheduledClassAddr, scheduledClass, strlen(scheduledClass));
  EEPROMWriteAnything(schedulesExecutionInfoAddr, (byte) ((numSchedulesExecuted * 2) + (schedulingRunning ? 1 : 0)), true);
  EEPROMWriteTwoDimensionalUnsignedLongArray(schedulesUnixTimeAddr, SCHEDULES_LEN, 2, (unsigned long*) schedulesUnixTime);
}

void writeSchedulingInfraredCommands() {
  EEPROMClear(EEPROM_SCHEDULING_INFRARED_COMMANDS_INITIAL_ADDR, EEPROM_SIZE_SCHEDULING_INFRARED_COMMANDS);

  EEPROMWriteUnsignedInt16Array(scheduleAirConditionerOnCommandAddr, SCHEDULE_AIR_CONDITIONER_ON_COMMAND_LEN, scheduleAirConditionerOnCommand);
  EEPROMWriteUnsignedInt16Array(scheduleAirConditionerOffCommandAddr, SCHEDULE_AIR_CONDITIONER_OFF_COMMAND_LEN, scheduleAirConditionerOffCommand);
  EEPROMWriteAnything(scheduleAirConditionerTemperatureAddr, scheduleAirConditionerTemperature, true);
}

boolean needEraseEEPROM() {
  char result = 'n';

  readFromSerial("Você precisa redefinir as configurações do dispositivo? (continua automaticamente após 5 segundos)[s/N]: ", &result, NULL, 1, 5000, false);

  if (result == 'S' || result == 's') {
    EEPROMClear(EEPROM_CONFIG_PARAMS_INITIAL_ADDR, EEPROM_SIZE_CONFIG_PARAMS);
    Serial.println("* Obs.: Digite '.' caso deseje permanecer com o valor anterior numa determinada configuração.");
    return true;
  }

  return false;
}

void EEPROMClear(int addr, int len) {
  char data[len];

  memset(data, '\0', len);
  EEPROMWriteCharArray(addr, data, len);
}

void EEPROMWriteCharArray(int addr, char* data, int len) {
  EEPROMBeginWrite(addr++);

  for (size_t i = 0; i < len; i++) {
    EEPROM.write(addr++, data[i]);
  }

  EEPROMEndWrite(addr, true);
}

void EEPROMWriteUnsignedInt16Array(int addr, size_t len, uint16_t* values) {
  EEPROMBeginWrite(addr++);

  for (size_t i = 0; i < len; i++) {
    EEPROMWriteAnything(addr, values[i], false);
    addr += sizeof(uint16_t);
  }

  EEPROMEndWrite(0, false);
}

void EEPROMWriteTwoDimensionalUnsignedLongArray(int addr, size_t rows, size_t cols, unsigned long* values) {
  EEPROMBeginWrite(addr++);

  for (size_t i = 0; i < rows; i++) {
    for (size_t j = 0; j < cols; j++) {
      EEPROMWriteAnything(addr, values[(i * cols) + j], false);
      addr += sizeof(unsigned long);
    }
  }

  EEPROMEndWrite(0, false);
}

int EEPROMReadCharArray(int addr, char* buf) {
  int count = -1;

  if (EEPROMBeginRead(addr++)) {
    do {
      buf[++count] = EEPROM.read(addr++);
    } while (buf[count] != EEPROM_END && count < EEPROM_SIZE);  
  }

  EEPROMEndRead();

  return count;
}

void EEPROMReadUnsignedInt16Array(int addr, size_t len, uint16_t* buf) {
  bool successfulBeginRead = EEPROMBeginRead(addr++);

  for (size_t i = 0; i < len; i++) {
    uint16_t result = 0U;

    if (successfulBeginRead) {
      EEPROMReadAnything(addr, result, false);
    }

    buf[i] = result;

    addr += sizeof(uint16_t);
  }

  EEPROMEndRead();
}

void EEPROMReadTwoDimensionalUnsignedLongArray(int addr, size_t rows, size_t cols, unsigned long* buf) {
  bool successfulBeginRead = EEPROMBeginRead(addr++);

  for (size_t i = 0; i < rows; i++) {
    for (size_t j = 0; j < cols; j++) {
      unsigned long result = 0UL;

      if (successfulBeginRead) {
        EEPROMReadAnything(addr, result, false);
      }

      buf[(i * cols) + j] = result;

      addr += sizeof(unsigned long);
    }
  }

  EEPROMEndRead();
}
void EEPROMBeginWrite(int addr) {
  EEPROM.begin(EEPROM_SIZE);
  EEPROM.write(addr, EEPROM_START);
}

void EEPROMEndWrite(int addr, boolean writeEndMarker) {
  if (writeEndMarker) {
    EEPROM.write(addr, EEPROM_END);
  }

  EEPROM.commit();
  EEPROM.end();
}

boolean EEPROMBeginRead(int addr) {
  EEPROM.begin(EEPROM_SIZE);

  return EEPROM.read(addr) == EEPROM_START;
}

void EEPROMEndRead() {
  EEPROM.end();
}

template <class T> void EEPROMWriteAnything(int addr, const T& value, bool beginAndEnd) {
  if (beginAndEnd) {
    EEPROMBeginWrite(addr++);
  }

  const byte* ptr = (const byte*)(const void*) &value;

  for (size_t i = 0; i < sizeof(value); i++) {
    EEPROM.write(addr++, *ptr++);
  }

  if (beginAndEnd) {
    EEPROMEndWrite(0, false);
  }
}

template <class T> void EEPROMReadAnything(int addr, T& buf, bool beginAndEnd) {
  if (!beginAndEnd || EEPROMBeginRead(addr++)) {
    byte* ptr = (byte*)(void*)&buf;

    for (size_t i = 0; i < sizeof(buf); i++) {
      *ptr++ = EEPROM.read(addr++);
    }
  }

  if (beginAndEnd) {
    EEPROMEndRead();
  }
}

