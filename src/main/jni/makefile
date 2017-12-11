# Define a variable for classpath
CLASS_PATH = ../../../target/classes
PATH1A = org/warp/picalculator
PATH1B = org.warp.picalculator

# Define a virtual path for .class in the bin directory
vpath %.class $(CLASS_PATH)/$(PATH1A)

all : picalculatornative.dll

# $@ matches the target, $< matches the first dependancy
picalculatornative.dll : TestJNI.o
	x86_64-w64-mingw32-gcc.exe -m64 -mtune=generic -Wl,--add-stdcall-alias -shared -o ../resources/$@ $<

# $@ matches the target, $< matches the first dependancy
TestJNI.o : TestJNI.c TestJNI.h
	x86_64-w64-mingw32-gcc.exe -m64 -mtune=generic -I"G:\Program Files\Java\jdk1.8.0_144\include" -I"G:\Program Files\Java\jdk1.8.0_144\include\win32" -c $< -o $@

# $* matches the target filename without the extension
TestJNI.h : TestJNI.class
	javah -o "$*.h" -classpath "$(CLASS_PATH)" $(PATH1B).$*

clean :
	rm TestJNI.h TestJNI.o ../resources/picalculatornative.dll