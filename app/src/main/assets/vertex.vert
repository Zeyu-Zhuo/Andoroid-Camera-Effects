attribute vec2 vCoord;
attribute vec4 vPosition;
varying vec2 aCoord;

void main(){
    gl_Position=vPosition;
    aCoord = vCoord;
}