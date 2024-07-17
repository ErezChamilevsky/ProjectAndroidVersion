The code for part 3 is under branch part3Main.
the implemntion of API for work with the server is under api directory (in src->main->java->api).
clone the project and run him from part3Main branch.

***important note:***

there maybe some functionality that not working in the second try (for example ,if you try to login and you put some wrong details, the server send you error mesaage and if you try to fix the wrong detail and send again to server it's sometimes send you again that somthing wrong despite the details are correct).

in this case please run whole app again and now things are work fine.

***Dont conclude from the first attempt that things do not work.***



To work in your network please enter to res->values->strings.xml ,and change ths string called "BaseUrl" ,in the part of ip definition (after "http") ,to your ip address in your network.

**Dont** change the Port number.

Now - load manualy the data to the mongo schemas - to do that - take this file [videos.json](https://github.com/user-attachments/files/16272176/videos.json) and insert to mongo schema tests/videos.
-- if you check the part 2 (Web) too ,**Dont** use the same videos there to this part!! , ***its not working.***

To run the server open this link https://github.com/ErezChamilevsky/youtubeProjectServer.git , clone, and write in Cmd "npm start".

**So for conclusion follow this order:**

1.load the videos.json to mongo.

2.run the server.

3.run the Android Application.
