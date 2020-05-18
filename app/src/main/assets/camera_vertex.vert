attribute vec4 vCoord;
attribute vec4 vPosition;
varying vec2 aCoord;
uniform mat4 vMatrix;

void main(){
    gl_Position=vPosition;
    aCoord = (vMatrix *vCoord).xy;
}