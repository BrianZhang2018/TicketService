TicketService-Walmart-lab
==

Building and Testing
==

Build:
``` shellsession
$ mvn clean install
```

Test with:
``` shellsession
$ mvn exec:java -D"exec.mainClass"="com.brian.ticketservice.App"
```

Test Output sample:

Please refer the `TestOutputSample.txt` in the repo


Assumptions
==
*The most front seat considers the best seat

*Every seat has a seat id

*A seat needs to be held and doesn't expire before be reserved

*Able to interact with customer from Console

*In the real world, need to consider the thread safe problem
