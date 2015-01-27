# mbta-gtfs-sonification

Project that generates music in real time based on the locations of the Boston train system.

This is my final project for EP-491 at Berklee College of Music.
The goal is to create a piece of software that generates enjoyable, and possibly useful music
in real time using the gtfs feed provided by the MBTA.

15.1.27
  So far, I have written code that takes the csv format static files provided by the MBTA and turns them into
  more easily accessible objects (routes, trips, trip shapes, stops, etc.)
  I have also looked at and edited the onebusaway visualizer project:
      (https://github.com/OneBusAway/onebusaway-gtfs-realtime-visualizer/wiki)
    to gain familliarity with the gtfs format as well as further my understanding of the Java language.
    
  next steps include:
    completeing the handling of static files.
    repurposing the current textInput.java class to no longer contain the main method,
      so that it can exist as a staticHandler class in the larger project.
   
  Once the statics, as well as the real time vehicle positions coexist within the same application I will
     output the data to Max/MSP to generate the composition.
