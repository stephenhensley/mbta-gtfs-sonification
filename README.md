# mbta-gtfs-sonification
THERE IS AN ISSUE ADDING THINGS TO THE GIT INDEX/COMMITTING FROM ECLIPSE RIGHT NOW.
Below discusses the project as it exists now, and as it will be commited here as soon as possible.

    MBTA Sonification
    Stephen Hensley
    shensley.Audio@gmail.com

TO RUN:


    open the max project located at ShensleyMbtaGtfs/max/MbtaProj/MbtaProj.maxproj
    cd MbtaProj/ShensleyMbtaGtfs/prototypes/
    java -jar shensleyMbtaGtfs_1-0-0.jar --vehiclePositionsUrl=http://developer.mbta.com/lib/GTRTFS/Alerts/VehiclePositions.pb
   
and then click on the top left preset in the Max Main window.
The presets are mostly blank and can be stored by shift clicking after changing the mix. 
The top row has many already used to explore each line's sound by itself.

  This software was designed with the intent of creating a musical sonification of the real time positions of the trains in Boston. It is comprised of two parts.
  The first part is a command-line application written in Java. This application receives the protobuffer feed from the MBTA, parses it, and compares it with Static Files provided by the MBTA to create and format a file that has useful JSON data for each subway train. 
  The second part is a Max project. It reads the JSON file written from the command-line application, and uses the data to compose and synthesize music in real time. The total number of trains has an effect on the tempo as well as the key signature of the piece. All trains are handled in pairs. For example, a Blue line train bound for Bowdoin would be paired with a Blue line train bound for Wonderland, and there are systems in place to deal with cases where there are different numbers of there are unequal numbers of trains. The Green line pairs generate chords and harmony. The Red line pairs generate bass notes and sound effects. The Blue line generates rhythms and percussion. The Orange line uses the notes being played by the Green line to generate melodic sequences.
  This is the first of many explorations into data-based composition and sonification. I believe there is a lot of art in the data that floods our lives, and I also believe sound can be powerful filter for exploring big data. While this project does work and could be looked at as complete, I'm already considering updates, as well as a possible redesign for a mobile application.
  
  
  
  If you have any questions, or want to talk about my project don't hesitate to contact me!

  I would also like to extend a huge thank you to my mentors at Berklee College of Music. Namely, Matthew Davidson, Dr. Richard Boulanger, Tom Zicarelli, and Neil Leonard. Without whom, this project would never have even been a thought, nevermind a working software system. Further, my friends and peers in Electronic Production and Design are some of the most inspring and intelligent people I have ever met, and they have constantly pushed me to push myself. All of these people deserve much more than a few words of thanks for being some of the best people I've ever had the pleasure of learning from, and with.
  
  ALSO credit where credit is due:
  I have looked at, dissected and repurposed parts of the onebusaway visualizer project inside of my project:
      (https://github.com/OneBusAway/onebusaway-gtfs-realtime-visualizer/wiki)
   

