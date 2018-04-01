import { Injectable } from '@angular/core';
import { UniqueDeviceID } from '@ionic-native/unique-device-id';
import { Paho } from 'ng2-mqtt/mqttws31';

@Injectable()
export class MqttConnectionProvider {
  private _client: Paho.MQTT.Client;

  onConnect: (client: Paho.MQTT.Client) => void;
  onMessageArrived: (message: Paho.MQTT.Message) => void;

  constructor(public uniqueDeviceID: UniqueDeviceID) {
	this.uniqueDeviceID.get()
	  .then((uuid: string) => {
        this._client = new Paho.MQTT.Client('192.168.0.101', 61614, 'SmartClassroomApp:' + uuid);

        this._client.connect({ onSuccess: _ => this.onConnect(this._client) });

		this._client.onMessageArrived = (message: Paho.MQTT.Message) => {
          this.onMessageArrived(message);
        };
	  })
	  .catch((err: any) => console.log(err));
  }

  sendMessage(message: string, topic: string) {
    let packet = new Paho.MQTT.Message(message);
    packet.destinationName = topic;
    this._client.send(packet);
  }
}
