`TicketService-Walmart-lab`
==

Building and Testing
==

Build:
``` shellsession
$ mvn clean install
```

Test with:
``` shellsession
$ mvn exec:java -D"exec.mainClass"="com.brian.ticketservice.App.java"
```

Test Output sample:


 0. Exit
 1. Show the number of available seats
 2. Hold seats
 3. Reserve held seats
Please select your option:

1
100 seats is available now

 0. Exit
 1. Show the number of available seats
 2. Hold seats
 3. Reserve held seats
Please select your option:

2
Please enter how many seats you want to hold:
10
Please enter your email address:
1@1.com
Congratulation, you have successful held the seats! The Id of your held seats is : 1
 Held seats:

Row: 1 ,Number:1
Row: 1 ,Number:2
Row: 1 ,Number:3
Row: 1 ,Number:4
Row: 1 ,Number:5
Row: 1 ,Number:6
Row: 1 ,Number:7
Row: 1 ,Number:8
Row: 1 ,Number:9
Row: 1 ,Number:10

 0. Exit
 1. Show the number of available seats
 2. Hold seats
 3. Reserve held seats
Please select your option:

3
Please enter Id of your held seats:
1
Please provide your email address:
1@1.com
Congratulation, you have successful reserved the seats! Your confirmation code is : d0e733fe-3130-4888-8114-1df3254cf8b9
 Reserved seats:

Row: 1 ,Number:1
Row: 1 ,Number:2
Row: 1 ,Number:3
Row: 1 ,Number:4
Row: 1 ,Number:5
Row: 1 ,Number:6
Row: 1 ,Number:7
Row: 1 ,Number:8
Row: 1 ,Number:9
Row: 1 ,Number:10


