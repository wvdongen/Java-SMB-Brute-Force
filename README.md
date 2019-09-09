# Java SMB Brute Force

## How to use
```
Usage: java -jar smb.jar [options]

hosts.txt:
 - All hosts to test
 -  This file should contain 1 host/ip address per line. 

user_pass.txt:
 - If this file exists usernames and passwords will be taken from this file and tested against all hosts in hosts.txt.
 - Format: username;password one per line.

Arguments:
 -d <arg> Domain to use

If user_pass.txt does not exist, the following arguments can be used to test a single username and password agains all 
hosts in hosts.txt: 
 -p <arg> Password to test
 -u <arg> Username to test

Working credentials will be appended to the file smb.txt.
```

By default 20 threads are used for brute forcing.