#version 300 es
precision mediump float;
out vec4 FragColor;
in vec2 TexCoord;
uniform sampler2D textureSamp;
void main()
{
    // FragColor = texture(textureSamp, TexCoord);

    FragColor = vec4(gl_FragCoord.zzz, 1.0);
}