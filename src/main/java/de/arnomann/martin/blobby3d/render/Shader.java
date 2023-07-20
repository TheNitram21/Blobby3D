package de.arnomann.martin.blobby3d.render;

import de.arnomann.martin.blobby3d.core.Blobby3D;
import de.arnomann.martin.blobby3d.math.Matrix4;

import static org.lwjgl.opengl.GL33.*;

public class Shader {

    private final int id;

    public Shader(String vertexSource, String fragmentSource) {
        id = createShader(vertexSource, fragmentSource);
        if(id == -1) {
            Blobby3D.getLogger().error("An error occured whilst trying to create the shader!");
        }
    }

    private int createShader(String vertexSource, String fragmentSource) {
        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
            int programId = glCreateProgram();

            int vertId = glCreateShader(GL_VERTEX_SHADER);
            glShaderSource(vertId, vertexSource);
            glCompileShader(vertId);
            if(glGetShaderi(vertId, GL_COMPILE_STATUS) == GL_FALSE) {
                String infoLog = glGetShaderInfoLog(vertId);
                Blobby3D.getLogger().error("Could not create the vertex shader! Log:\n" + infoLog);
                glDeleteShader(vertId);
                return -1;
            }

            int fragId = glCreateShader(GL_FRAGMENT_SHADER);
            glShaderSource(fragId, fragmentSource);
            glCompileShader(fragId);
            if(glGetShaderi(fragId, GL_COMPILE_STATUS) == GL_FALSE) {
                String infoLog = glGetShaderInfoLog(fragId);
                Blobby3D.getLogger().error("Could not create the fragment shader! Log:\n" + infoLog);
                glDeleteShader(fragId);
                return -1;
            }

            glAttachShader(programId, vertId);
            glAttachShader(programId, fragId);
            glBindAttribLocation(programId, 0, "in_Position");
            glBindAttribLocation(programId, 1, "in_TextureCoords");
            glLinkProgram(programId);
            glValidateProgram(programId);

            glDeleteShader(vertId);
            glDeleteShader(fragId);

            return programId;
        }

        return -1;
    }

    public void setUniform1f(String name, float value) {
        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
            int location = glGetUniformLocation(id, name);
            if (location != -1) {
                glUniform1f(location, value);
            }
        }
    }

    public void setUniform2f(String name, float x, float y) {
        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
            int location = glGetUniformLocation(id, name);
            if(location != -1) {
                glUniform2f(location, x, y);
            }
        }
    }

    public void setUniform3f(String name, float x, float y, float z) {
        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
            int location = glGetUniformLocation(id, name);
            if(location != -1) {
                glUniform3f(location, x, y, z);
            }
        }
    }

    public void setUniform1i(String name, int value) {
        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
            int location = glGetUniformLocation(id, name);
            if(location != -1) {
                glUniform1i(location, value);
            }
        }
    }

    public void setUniform2i(String name, int x, int y) {
        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
            int location = glGetUniformLocation(id, name);
            if(location != -1) {
                glUniform2i(location, x, y);
            }
        }
    }

    public void setUniform3i(String name, int x, int y, int z) {
        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
            int location = glGetUniformLocation(id, name);
            if(location != -1) {
                glUniform3i(location, x, y, z);
            }
        }
    }

    public void setUniformMatrix4(String name, Matrix4 matrix) {
        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
            int location = glGetUniformLocation(id, name);
            if(location != -1) {
                glUniformMatrix4fv(location, false, matrix.toFloatBuffer());
            }
        }
    }
//    public void setUniformMatrix4(String name, Matrix4f matrix) {
//        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
//            int location = glGetUniformLocation(id, name);
//            try(MemoryStack stack = MemoryStack.stackPush()) {
//                if(location != -1) {
//                    glUniformMatrix4fv(location, false, matrix.get(stack.mallocFloat(16)));
//                }
//            }
//        }
//    }

    public void bind() {
        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL)
            glUseProgram(id);
    }

}
