void initSerial() {
  Serial.begin(115200);
}

boolean readFromSerial(const char* prompt, char* buf, char* oldValue, int maxLen, int timeout, boolean hideInput) {
  int timer = 0;
  int delayTime = 1000;
  String input = "";

  if (maxLen <= 0) {
    return false;
  }

  Serial.print(prompt);

  while(1) {
    input = Serial.readString();

    int len = input.length();

    if (len > maxLen) {
      Serial.println();
      Serial.printf("Você deve digitar no máximo %d caractere(s), atualmente o texto tem %d caractere(s).\r\n", maxLen, len);
    } else if (len > 0) {
      if (input.equals(".")) {
        strcpy(buf, oldValue);
        len = strlen(buf);
      } else {
        sprintf(buf, "%s", input.c_str());
      }

      if (hideInput) {
        for (size_t i = 0; i < len; i++) {
          Serial.print("*");
        }
        Serial.println();
      } else {
        Serial.println(buf);
      }

      return true;
    }

    timer += delayTime;

    if (timeout > 0 && timer >= timeout) {
      Serial.println("\r\nVocê não digitou nada, continuando...");
      return false;
    }

    delay(delayTime);
  }
}

