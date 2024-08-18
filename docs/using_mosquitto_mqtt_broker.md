# Using Mosquitto MQTT Broker

Mosquitto is an open source message broker that implements the MQTT protocol.
[https://mosquitto.org/](https://mosquitto.org/)

## 1. Installing Mosquitto on Ubuntu 24 Server

```shell
sudo apt-get update
sudo apt-get install mosquitto
sudo apt-get install mosquitto-clients
```

Make sure the mosquitto service is active(running)

```shell
sudo systemctl status mosquitto.service     # Read service status
```

**Extra commands**



```shell
# Manage service commands
sudo systemctl start mosquitto.service      # Start service
sudo systemctl stop mosquitto.service       # Stop service
sudo systemctl restart mosquitto.service    # Restart service
sudo systemctl enable mosquitto.service     # Enale to start when the system init

# See service logs
sudo journalctl -u mosquitto -f

# Running mosquitto via command line (service must be stopped)
sudo mosquitto -c /etc/mosquitto/mosquitto.conf

# Checking mosquitto version
mosquitto                           # This command try to run the mosquitto software

# Verifying if the port 1883 is listen
sudo ss -tulnp | grep 1883
```

### 1.1 Creating configuration file

Create the configuration file `default.conf`.

```shell
sudo nano /etc/mosquitto/conf.d/default.conf
```

.. and edit this file writting the content below.

```shell
listener 1883
allow_anonymous false
password_file /etc/mosquitto/passwd
```

Create the password file

```shell
sudo touch /etc/mosquitto/passwd
sudo chmod 700 /etc/mosquitto/passwd
sudo chown mosquitto: /etc/mosquitto/passwd
```

Restart the vervice

```shell
sudo systemctl restart mosquitto.service
```

## 2 Creating users and passwords

Steps to manage users.

1. Change the password file permission

```shell
sudo chown root: /etc/mosquitto/passwd
```

2. Manage users (add/delete)
<br>

3. Change the password file permission

```shell
sudo chown mosquitto: /etc/mosquitto/passwd

```

4. Restart service

```shell
sudo systemctl restart mosquitto.service
```

**Create the password file**

```shell
sudo mosquitto_passwd -c /etc/mosquitto/passwd <username>
```

You will be prompted to type the password

```shell
Password:
Reenter password:
```

A new line will be added to the password file

```shell
leo:$7$101$AFQuBQ0fGGzoaRa5$eJ4Ny0jq+1pNg8NE8g/Y9BOjlKStt5EAKKb4aRSXRa4Gwe7xNsI0PWJRWh0tnAkTasGMYwCodqxK36yXc9vuPA==
```

**Add user**

```shell
sudo mosquitto_passwd /etc/mosquitto/passwd <newuser>
```

**Delete user**

Edit the password file and remove the line of the user

```shell
sudo nano /etc/mosquitto/passwd
```

## 3. Access Control List (ACL)

Restricting users that can access a `topic`.

1. Create the ACL file

```shell
sudo touch /etc/mosquitto/acl
```

2. Add acf file to the configuration file

```shell
sudo nano /etc/mosquitto/conf.d/default.conf
```

```shell
acl_file /etc/mosquitto/acl
```

3. Edit the acl file

```shell
sudo nano /etc/mosquitto/acl
```

```shell
# This affects access control for clients with no username.
topic read $SYS/#

# This only affects clients with username "leo" and "admin".
user leo
topic write output
user admin
topic readwrite output

# This affects all clients.
pattern write $SYS/broker/connection/%c/state
```

It is also possible define ACLs based on pattern:

```shell
pattern [read|write|readwrite|deny] <topic>
```

```shell
%c   # Client ID
%u   # Username
```

```shell
# Example
pattern write sensor/%u/data
```

4. Restart service

```shell
sudo systemctl restart mosquitto.service
```