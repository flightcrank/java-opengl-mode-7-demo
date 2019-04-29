
# Java OpenGL Mode 7 Demo
This is a simple Java program that replicates the functions of the super Nintendo's background mode 7.
It operations on a texture and preforms affine matrix operations.

* scaling
* shearing
* rotation
* translation

These operations on a per pixel basis on the CPU are incredibly slow. They need to be done on the GPU
for better performance. This program uses vertex and pixel sharders for OpenGL 3.3

Its currently still under active development. I would like to implement the per scanline texture scaling
that mode 7 is known for in the future.

This Java program is dependant on JOGL being available to your class path. If not you can download the JOGL
library's and specify the JOGL jar files at compile time.

Compilation instructions are as follows...

	$ javac -cp ../jogl/jar/gluegen-rt.jar:../jogl/jar/jogl-all.jar:./ Mode7Demo.java

That should compile all necessary .java files. to run the program type the command

	$ java -cp ../jogl/jar/gluegen-rt.jar:../jogl/jar/jogl-all.jar:./ Mode7Demo

## Images
![Imgur](https://i.imgur.com/qhvdg7n.png)

![Imgur](https://i.imgur.com/St0US0Q.gif)
