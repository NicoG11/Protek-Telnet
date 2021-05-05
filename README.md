# Project is outdated and not for public use

# Protek 9760 HD IP Remote Control Connection by Telnet

Protek 9760 HD IP Telnet connection class for remote control

Remote Key Map (BorisFirmware)
```
+---------------+-------------------------------+---------------+
|         Power |                               | M        Mute |
+---------------+---------------+---------------+---------------+
| F9   V.Format | F10       Sat | F11    TV/STB | F12  TV/Radio |
+---------------+-----+---------+---------+-----+---------------+
|          1          |         2         |          3          |
+---------------------+-------------------+---------------------+
|          4          |         5         |          6          |
+---------------------+-------------------+---------------------+
|          7          |         8         |          9          |
+---------------------+-------------------+---------------------+
| TAB            Prev |         0         | f          Favorite |
+---------------+-----+---------+---------+-----+---------------+
| V     Volume+ | e         EPG | i        Info | C    Channel+ |
|               +---------------+---------------+               |
| v     Volume- |                               | c    Channel- |
+---------------+             Enter             +---------------+
| x        Exit |                               | m        Menu |
+---------------+---------------+---------------+---------------+
| a       Audio | t    TeleText | s    Subtitle | +      Option |
+---------------+---------------+---------------+---------------+
| F5        RED | F6      GREEN | F7     Yellow | F8       Blue |
+---------------+---------------+---------------+---------------+
| <      Rewind | Space    Play | End      Stop | >         F.F |
+---------------+---------------+---------------+---------------+
| u         USB | |       Pause | Ins       Rec | Home     Live |
+---------------+---------------+---------------+---------------+
```

## TelnetClient in Java + MyElectronApp


Convert Hex to UTF8 to get all this keys

=00000075 USB     
=0000002B OPTION  
=0000000D OK  
=001B5B42 DOWN  
=001B5B41 UP  
=001B5B43 RIGHT  
=001B5B44 LEFT  
=00000009 PREVIOUS (TAB)  
=1B5B3135 RED     (F5)  
=1B5B3137 GREEN   (F6)  
=1B5B3138 YELLOW  (F7)  
=1B5B3139 BLUE    (F8)  

=1B5B3230 V.Format	(F9)  
=1B5B3231 SAT 		  (F10)  
=1B5B3233 TV_STB 	  (F11)  
=1B5B3234 TV_RADIO	(F11)  

=00000020 PLAY 	(Space)  
=001B5B32 REC   (Ins)  
=001B5B31 LIVE 	(Home / Pos1)  
=0000003E FF 	  (>)  
=0000003C REW	  (<)  
=001B5B34 STOP 	(Ende)  

=0000001B EXIT  

