package com.UKC_AICS.simulation.screen.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Class used in order to render the {@link com.UKC_AICS.simulation.screen.graphics.TileMesh meshes}
 * used to represent variable environment layers.
 *
 * @author Ben Nicholls bn65@kent.ac.uk
 */
public class MeshRenderer  {


	public static final String VERT_SHADER =
	            "attribute vec2 a_position;\n" +
	            "attribute vec4 a_color;\n" +
	            "attribute vec2 a_texCoords;\n" +
	            "uniform mat4 u_projTrans;\n" +
	            "varying vec4 vColor;\n" +
	            "varying vec2 vTexCoords;\n" +
	            "void main() {\n" +
	            " vColor = a_color;\n" +
	            " vTexCoords = a_texCoords;\n" +
	            " gl_Position = u_projTrans * vec4(a_position.xy, 0.0, 1.0);\n" +
	            "}";
	 public static final String FRAG_TEXTURESHADER =
            "#ifdef GL_ES\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec4 vColor;\n" +
            "varying vec2 vTexCoords;\n" +
            "uniform sampler2D u_texture;\n" +
            "uniform mat4 u_projTrans;\n" +
            "void main() {\n" +
            "// gl_FragColor = vColor;\n" +
            "gl_FragColor = vColor * texture2D(u_texture, vTexCoords);\n" +
            "}";

	 private static final ShaderProgram textureShader = createMeshShader();
	 private final Array<TileMesh> meshes = new Array<TileMesh>();

    /**
     * Used to construct the ShaderProgram for the renderer
     * @return ShaderProgram for rendering meshes
     */
	 protected static ShaderProgram createMeshShader() {
            ShaderProgram.pedantic = false;
            ShaderProgram shader;
            shader = new ShaderProgram(VERT_SHADER, FRAG_TEXTURESHADER);
            String log = shader.getLog();
            if (!shader.isCompiled())
                throw new GdxRuntimeException(log);
            if (log!=null && log.length()!=0)
                System.out.println("Shader Log: "+log);
            return shader;
    }


    private Texture blankTex = new Texture(new Pixmap(16, 16, Pixmap.Format.RGBA8888){{
        setColor(Color.WHITE);
        fill();
    }});

    /**
     * Renders a single {@link com.UKC_AICS.simulation.screen.graphics.TileMesh TileMesh}.
     * @param mesh TileMesh to render
     * @param cam Camera to render the mesh in
     */
    public void renderMesh(TileMesh mesh, Camera cam){
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //camera stuff
        //            Gdx.gl.glViewport((int) glViewport.x, (int) glViewport.y, (int) glViewport.width, (int) glViewport.height);
        cam.update();

        //no need for depth...
        Gdx.gl.glDepthMask(false);
        //enable blending, for alpha
        Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


        //number of vertices we need to render
        int vertexCount = mesh.getVertexCount();//.getIndex()/gridMesh.getNumVertexComponents();
        textureShader.begin();
//        blankTex.bind();
        if(mesh.texture != null)
            mesh.texture.bind();
        else
            blankTex.bind();
        //update the projection matrix so our triangles are rendered in 2D
        textureShader.setUniformMatrix("u_projTrans", cam.combined);
        textureShader.setUniformi("u_texture", 0);
        //render the mesh
        mesh.mesh.render(textureShader, GL20.GL_TRIANGLES, 0, vertexCount);
        textureShader.end();
        Gdx.gl.glDepthMask(true);
    }

    /**
     * Render a number of {@link com.UKC_AICS.simulation.screen.graphics.TileMesh TileMeshes}.
     * @param meshes Array of meshes to render
     * @param cam Camera to render the meshes in
     */
    public void renderMeshes(TileMesh meshes[], Camera cam){

        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //camera stuff
//            Gdx.gl.glViewport((int) glViewport.x, (int) glViewport.y, (int) glViewport.width, (int) glViewport.height);
        cam.update();

        //no need for depth...
//        Gdx.gl.glDepthMask(false);
        //enable blending, for alpha
        Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        //number of vertices we need to render
        //start the textureShader before setting any uniforms
        textureShader.begin();

        for(TileMesh mesh : meshes){
            Texture t = mesh.texture;
            if(t != null)
                t.bind();
            else
                blankTex.bind();
            //update the projection matrix so our triangles are rendered in 2D
            textureShader.setUniformMatrix("u_projTrans", cam.combined);
            textureShader.setUniformi("u_texture", 0);

            mesh.mesh.render(textureShader, GL20.GL_TRIANGLES, 0, mesh.getVertexCount());
        }
        textureShader.end();
        //re-enable depth to reset states to their default
//        Gdx.gl.glDepthMask(true);
    }
	        
}
