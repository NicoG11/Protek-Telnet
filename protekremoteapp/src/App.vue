<template>
  <v-app>
    <v-toolbar app>
      <v-toolbar-title class="headline text-uppercase">
        <span>Nic0G: </span>
        <span class="font-weight-light">Protek Remote App</span>
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <span>
        <v-switch
          v-model="connected"
          :label="connected? 'Verbindung online': 'Verbindung offline'"
          color="success"
          hide-details
          @click.stop="connect()"
        ></v-switch>
      </span>
      <v-spacer></v-spacer>
      <span class="mr-2">Version: 1.0.0</span>
    </v-toolbar>

    <v-content>
      <v-container>
        <v-layout
          text-xs-center
          wrap
        >
          <v-flex my-4>
            <h1 class="display-2 font-weight-bold mb-3">
              Remote Control
            </h1>
          </v-flex>
        </v-layout>
      </v-container>
      <v-container
        grid-list-xl
        text-xs-center
      >
        <v-layout
          row
          wrap
        >
          <template
            v-for="item in this.keymap"
          >
            <v-flex
              :key="item.id"
              :class="item.layout"
            >
              <v-btn
                @click.stop.prevent="sendCommand(item.cmd)"
              >
                {{item.title}}
              </v-btn>
            </v-flex>
          </template>
          <v-flex
            mb-5
            xs12
          >
            <h2 class="headline font-weight-bold mb-3">Remote-Terminal</h2>
            <v-layout justify-center>
              <v-textarea
                v-model="terminalOutput"
                name="output"
                label="Terminal output"
                box
              ></v-textarea>
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
      showTastatur: false,
      terminalOutput: '',
      keymap: [
        {id: 1, layout: 'xs3', title: 'Power', cmd: ''},
        {id: 2, layout: 'xs3 offset-xs6', title: 'Mute', cmd: 'M'},
        {id: 3, layout: 'xs3', title: 'V.Format', cmd: '\u001b[20'},
        {id: 4, layout: 'xs3', title: 'Sat', cmd: '\u001b[21'},
        {id: 5, layout: 'xs3', title: 'TV/STB', cmd: '\u001b[23'},
        {id: 6, layout: 'xs3', title: 'TV/Radio', cmd: '\u001b[24'},
        {id: 7, layout: 'xs4', title: '1', cmd: '1'},
        {id: 8, layout: 'xs4', title: '2', cmd: '2'},
        {id: 9, layout: 'xs4', title: '3', cmd: '3'},
        {id: 10, layout: 'xs4', title: '4', cmd: '4'},
        {id: 11, layout: 'xs4', title: '5', cmd: '5'},
        {id: 12, layout: 'xs4', title: '6', cmd: '6'},
        {id: 13, layout: 'xs4', title: '7', cmd: '7'},
        {id: 14, layout: 'xs4', title: '8', cmd: '8'},
        {id: 15, layout: 'xs4', title: '9', cmd: '9'},
        {id: 16, layout: 'xs4', title: 'Prev', cmd: '\t'},
        {id: 17, layout: 'xs4', title: '0', cmd: '0'},
        {id: 18, layout: 'xs4', title: 'Favorite', cmd: 'f'},
        {id: 19, layout: 'xs3', title: 'Volume +', cmd: 'V'},
        {id: 20, layout: 'xs3', title: 'EPG', cmd: 'e'},
        {id: 21, layout: 'xs3', title: 'Info', cmd: 'i'},
        {id: 22, layout: 'xs3', title: 'Channel +', cmd: 'C'},
        {id: 23, layout: 'xs3', title: 'Volume -', cmd: 'v'},
        {id: 24, layout: 'xs6', title: 'Up', cmd: '\u001b[A'},
        {id: 25, layout: 'xs3', title: 'Channel -', cmd: 'c'},
        {id: 26, layout: 'xs3', title: 'Left', cmd: '\u001b[D'},
        {id: 28, layout: 'xs6', title: 'Enter / OK', cmd: '\r\n'},
        {id: 29, layout: 'xs3', title: 'Right', cmd: '\u001b[C'},
        {id: 30, layout: 'xs3', title: 'Exit', cmd: 'x'},
        {id: 31, layout: 'xs6', title: 'Down', cmd: '\u001b[B'},
        {id: 32, layout: 'xs3', title: 'Menü', cmd: 'm'},
        {id: 33, layout: 'xs3', title: 'Audio', cmd: 'a'},
        {id: 34, layout: 'xs3', title: 'TeleText', cmd: 't'},
        {id: 35, layout: 'xs3', title: 'Subtitle', cmd: 's'},
        {id: 36, layout: 'xs3', title: 'Option', cmd: '+'},
        {id: 37, layout: 'xs3', title: 'RED', cmd: '\u001b[15'},
        {id: 38, layout: 'xs3', title: 'GREEN', cmd: '\u001b[17'},
        {id: 39, layout: 'xs3', title: 'YELLOW', cmd: '\u001b[18'},
        {id: 40, layout: 'xs3', title: 'BLUE', cmd: '\u001b[19'},
        {id: 41, layout: 'xs3', title: 'Rewind', cmd: '<'},
        {id: 42, layout: 'xs3', title: 'Play', cmd: ' '},
        {id: 43, layout: 'xs3', title: 'Stop', cmd: '\u001b[4~'},
        {id: 44, layout: 'xs3', title: 'F.F', cmd: '>'},
        {id: 45, layout: 'xs3', title: 'USB', cmd: 'u'},
        {id: 46, layout: 'xs3', title: 'Pause', cmd: '|'},
        {id: 47, layout: 'xs3', title: 'Record', cmd: '\u001b[2~'}, // insert
        {id: 48, layout: 'xs3', title: 'Live', cmd: '\u001b[1~'}, //pos1 / home
        {id: 49, layout: 'xs3', title: 'Hidden Menü', cmd: 'u+'}
      ]
    }
  },
  methods: {
    connect: async function () {
      let $vm = this
      if (!this.connected) {
        Client = await Net.createConnection(PORT, HOST);

        Client.on('connect', function () {
          $vm.connected = true;
        });
        // Add a 'data' event handler for the client socket
        // data is what the server sent to this socket
        Client.on('data', this.receiveData);

        // Add a 'close' event handler for the client socket
        Client.on('close', function() {
          $vm.connected = false;
          $vm.rootLevel = true;
          $vm.remoEmu= false;
          $vm.showTastatur= false;
          $vm.terminalOutput = "";
        });
        // Add a 'close' event handler for the client socket
        Client.on('end', function(){
          $vm.terminalOutput += 'End Event'
        });

        Client.on('error', function(error) {
          $vm.terminalOutput += error.toString()
        });

      } else {
        $vm.closeConnection();
      }
    },
    receiveData: async function (data) {
      this.terminalOutput = this.terminalOutput + data.toString();
      if (this.rootLevel) {
        if (await Client.write('remo\r\n')) {
          this.rootLevel = false;
        }
      }
    },
    sendCommand: async function (cmd) {
      await Client.write(cmd);
    },
    closeConnection: function () {
      if (this.connected && Client) {
        Client.end();
      }

    }
  }
}
</script>
