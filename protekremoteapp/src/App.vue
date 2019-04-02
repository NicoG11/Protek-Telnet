<template>
  <v-app>
    <v-toolbar app>
      <v-toolbar-title class="headline text-uppercase">
        <span>Nic0G: </span>
        <span class="font-weight-light">Protek Remote App</span>
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <span class="mr-2">Version: 0.1.0</span>
    </v-toolbar>

    <v-content>
      <v-container>
        <v-layout
          text-xs-center
          wrap
        >
          <v-flex xs12>
            Verbunden? {{connected? 'Ja':'Nein'}}
            <v-btn
              v-show="!connected"
              @click.stop.prevent="connect()"
            >
              Mit Reciver Verbinden
            </v-btn>
            <v-btn
              v-show="connected"
              @click.stop.prevent="closeConnection()"
            >
              Reciver-Verbindung beenden
            </v-btn>
          </v-flex>

          <v-flex mb-4>
            <h1 class="display-2 font-weight-bold mb-3">
              Nicos Protek Remote App
            </h1>
            <p class="subheading font-weight-regular">
              Fernbedienungslayout
            </p>
              <v-btn @click.stop="sendCommand('1\r\n')">
                1
              </v-btn>
              <v-btn @click.stop="sendCommand('2\r\n')">
                2
              </v-btn>
              <v-btn @click.stop="sendCommand('3\r\n')">
                3
              </v-btn>
              <v-btn @click.stop="sendCommand('4\r\n')">
                4
              </v-btn>
              <v-btn @click.stop="sendCommand('5\r\n')">
                5
              </v-btn>
              <v-btn @click.stop="sendCommand('6\r\n')">
                6
              </v-btn>
              <v-btn @click.stop="sendCommand('7\r\n')">
                7
              </v-btn>
              <v-btn @click.stop="sendCommand('8\r\n')">
                8
              </v-btn>
              <v-btn @click.stop="sendCommand('9\r\n')">
                9
              </v-btn>
              <v-btn @click.stop="sendCommand('0\r\n')">
                0
              </v-btn>
              <v-btn @click.stop="sendCommand('x')">
                Exit
              </v-btn>
              <v-btn @click.stop="sendCommand('C')">
                Channel +
              </v-btn>
              <v-btn @click.stop="sendCommand('c')">
                Channel -
              </v-btn>
              <v-btn @click.stop="sendCommand('u')">
                USB
              </v-btn>
              <v-btn @click.stop="sendCommand('f')">
                Favorite
              </v-btn>
              <v-btn @click.stop="sendCommand('+')">
                Option
              </v-btn>
              <v-btn @click.stop="sendCommand('u+')">
                Hidden Menü
              </v-btn>
              <v-btn @click.stop="sendCommand('[C')">
                Right
              </v-btn>
              <v-btn @click.stop="sendCommand('[D')">
                Left
              </v-btn>
              <v-btn @click.stop="sendCommand('[B')">
                Down
              </v-btn>
              <v-btn @click.stop="sendCommand('[A')">
                Top
              </v-btn>

              <v-btn @click.stop="sendCommand('\r\n')">
                Enter
              </v-btn>

          </v-flex>

          <v-flex
            mb-5
            xs12
          >
            <h2 class="headline font-weight-bold mb-3">Remote-Terminal</h2>

            <v-layout justify-center>
              Add Terminal output here
            </v-layout>
          </v-flex>

          <v-flex
            xs12
            mb-5
          >
            <h2 class="headline font-weight-bold mb-3">Sonstieges</h2>

            <v-layout justify-center>

            </v-layout>
          </v-flex>

        </v-layout>
      </v-container>
    </v-content>
  </v-app>
</template>

<script>

const Net = require('net');
let Client = null;
const HOST = '192.168.2.23';
const PORT = 23;


export default {
  name: 'App',
  data () {
    return {
      connected: false,
      rootLevel: true,
      remoEmu: false,
      showTastatur: false
    }
  },
  methods: {
    connect: async function () {
      let $vm = this
      if (!this.connected) {
        Client = await Net.createConnection(PORT, HOST);

        Client.on('connect', function () {
          $vm.connected = true;
          console.log('connected success');
        });
        // Add a 'data' event handler for the client socket
        // data is what the server sent to this socket
        Client.on('data', this.receiveData);

        Client.on('drain', function () {
          console.log('drain -Buffer empty');
        });
        // Add a 'close' event handler for the client socket
        Client.on('close', function(args) {
          console.log('close');
          $vm.connected = false;
          $vm.rootLevel = true;
          $vm.remoEmu= false,
          $vm.showTastatur= false
        });
        // Add a 'close' event handler for the client socket
        Client.on('end', function(){
          console.log('ende');
        });

        Client.on('error', function(error) {
          console.log(error);
        });

      }
    },
    receiveData: async function (data) {
      console.log('DATA: ' + data.toString());
      if (this.rootLevel) {
        if (await Client.write('remo\r\n')) {
          this.rootLevel = false;
        }
      }
    },
    sendCommand: async function (cmd) {
      // '\r\n' = Enter
      await Client.write(cmd);
    },
    closeConnection: function () {
      Client.end();
    }
  },
  mounted: async function () {
  }
}
</script>
