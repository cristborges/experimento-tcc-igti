import { Component } from '@angular/core';
import { Storage } from '@ionic/storage';
import { NavController, ModalController, Platform, NavParams, ViewController, AlertController } from 'ionic-angular';
import { Paho } from 'ng2-mqtt/mqttws31';
import { MqttConnectionProvider } from '../../providers/mqtt-connection/mqtt-connection';
import { Ambiente } from '../../models/ambiente';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {
  private _idsSalas: string[];

  ambientes: Ambiente[];

  constructor(
    public navCtrl: NavController,
    public modalCtrl: ModalController,
    public alertCtrl: AlertController,
    public storage: Storage,
    public platform: Platform,
	public mqttConnectionProvider: MqttConnectionProvider
  ) {
    this.ambientes = [];
    this._idsSalas = [];

    storage.get('smartClassroomApp:idsSalas').then((idsSalas: string[]) => {
      if (idsSalas) {
        idsSalas.forEach((idSala: string) => {
          let ambiente: Ambiente = new Ambiente();
          ambiente.sala = idSala;
		  ambiente.temperaturaArCondicionado = 22;
          this.ambientes.push(ambiente);
          this._idsSalas.push(idSala);
        });
      }
    });

    this.mqttConnectionProvider.onConnect = (client: Paho.MQTT.Client) => {
      client.subscribe('Classroom/Info', {});
    };

    this.mqttConnectionProvider.onMessageArrived = (message: Paho.MQTT.Message) => {
      let ambienteAtual: Ambiente = JSON.parse(message.payloadString);
      let ambiente = this._obterAmbientePeloIDSala(ambienteAtual.sala);

      if (ambiente && !ambiente.aguardandoExecucaoComando) {
        this.ambientes.splice(this.ambientes.indexOf(ambiente), 1, ambienteAtual);
      }
    };
  }

  alterarEstadoArCondicionado(ambiente: Ambiente) {
    ambiente.aguardandoExecucaoComando = true;

    let message: Ambiente = new Ambiente();
    message.sala = ambiente.sala;
    message.arCondicionadoLigado = ambiente.arCondicionadoLigado;

    this.mqttConnectionProvider.sendMessage(JSON.stringify(message), 'Classroom/AirConditioner/Status/Set');

    setTimeout(() => {
      ambiente.aguardandoExecucaoComando = false;
    }, 5000);
  }

  abrirModalAdicionarAmbiente() {
    let modalAdicionarAmbiente = this.modalCtrl.create(ModalAdicionarAmbiente);

    modalAdicionarAmbiente.onDidDismiss(ambiente => {
      if (ambiente) {
        if (this._obterAmbientePeloIDSala(ambiente.sala)) {
          this.alertCtrl.create({
            title: 'Ambiente Existente!',
            message: 'Este ambiente já foi adicionado!',
            buttons: ['Ok']
          }).present();
        } else {
          this.ambientes.push(ambiente);

          this._idsSalas.push(ambiente.sala);
          this.storage.set('smartClassroomApp:idsSalas', this._idsSalas);
        }

      }
    });

    modalAdicionarAmbiente.present();
  }

  removerAmbiente(ambiente: Ambiente) {
    let indexOfAmbiente = this.ambientes.indexOf(ambiente);

    this.ambientes.splice(indexOfAmbiente, 1);

    this._idsSalas.splice(indexOfAmbiente, 1);
    if (this._idsSalas.length === 0) {
      this.storage.remove('smartClassroomApp:idsSalas');
    } else {
      this.storage.set('smartClassroomApp:idsSalas', this._idsSalas);
    }
  }

  private _obterAmbientePeloIDSala(idSala: string) {
    let resultado: Ambiente = null;

    this.ambientes.forEach((ambiente) => {
      if (ambiente.sala === idSala) {
        resultado = ambiente;
      }
    });

    return resultado;
  }
}

@Component({
  template: `
    <ion-header>
      <ion-toolbar>
        <ion-title>
          Novo Ambiente
        </ion-title>
        <ion-buttons start>
          <button ion-button (click)="dismiss()">
            <span ion-text color="primary" showWhen="ios">Cancelar</span>
            <ion-icon name="md-close" showWhen="android, windows"></ion-icon>
          </button>
        </ion-buttons>
      </ion-toolbar>
    </ion-header>
    <ion-content>
      <ion-list>
        <ion-item *ngIf="ambiente">
          <ion-label floating>ID Sala</ion-label>
          <ion-input type="text" [(ngModel)]="ambiente.sala"></ion-input>
        </ion-item>
      </ion-list>
      <div padding>
        <button ion-button color="light" block (click)="adicionarAmbiente()" [disabled]="!ambiente.sala">Adicionar</button>
      </div>
    </ion-content>
  `
})
export class ModalAdicionarAmbiente {
  ambiente: Ambiente;

  constructor(public platform: Platform, public params: NavParams, public viewCtrl: ViewController) {
    this.ambiente = new Ambiente();
	this.ambiente.temperaturaArCondicionado = 22;
  }

  dismiss() {
    this.viewCtrl.dismiss();
  }

  adicionarAmbiente() {
    this.viewCtrl.dismiss(this.ambiente);
  }
}
