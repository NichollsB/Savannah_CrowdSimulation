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
    public static final String FRAG_COLORSHADER =
            "#ifdef GL_ES\n" +
                    "precision mediump float;\n" +
                    "#endif\n" +
                    "varying vec4 vColor;\n" +
                    "varying vec2 vTexCoords;\n" +
                    "uniform sampler2D u_texture;\n" +
                    "uniform mat4 u_projTrans;\n" +
                    "void main() {\n" +
                    "gl_FragColor = vColor;\n" +
                    "//gl_FragColor = vColor * texture2D(u_texture, vTexCoords);\n" +
                    "}";

	 private static final ShaderProgram textureShader = createMeshShader(true);
     private static final ShaderProgram colorShader = createMeshShader(false);
	 private final Array<TileMesh> meshes = new Array<TileMesh>();
	 protected static ShaderProgram createMeshShader(boolean textureShader) {
            ShaderProgram.pedantic = false;
            ShaderProgram shader;
             if(textureShader)
                shader = new ShaderProgram(VERT_SHADER, FRAG_TEXTURESHADER);
            else
                shader = new ShaderProgram(VERT_SHADER, FRAG_COLORSHADER);
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

	 public int addMesh(TileMesh mesh){
		 meshes.add(mesh);
		 return meshes.indexOf(mesh, true);
	 }
	 public int[] addMesh(TileMesh mesh[]){
		 meshes.addAll(meshes);
		 int indices[] = new int[mesh.length];
		 for(int i = 0; i < mesh.length; i ++){
			 indices[i] = meshes.indexOf(mesh[i], true);
		 }
		 return indices;
	 }
	 public void clearAll(){
		 meshes.clear();
	 }
	 public void clear(int index){
		 meshes.removeIndex(index);
	 }

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
//    Texture defaultTex = new Texture();
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
	        
	 public void renderAll(Camera cam){
         //make a copy of the grid
//            System.out.println("grid copy");

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
         //start the textureShader before setting any uniforms
         textureShader.begin();

         for(TileMesh mesh : meshes){
             mesh.texture.bind();
             //update the projection matrix so our triangles are rendered in 2D
             textureShader.setUniformMatrix("u_projTrans", cam.combined);
             textureShader.setUniformi("u_texture", 0);

             mesh.mesh.render(textureShader, GL20.GL_TRIANGLES, 0, mesh.getVertexCount());
         }
         textureShader.end();
         //re-enable depth to reset states to their default
         Gdx.gl.glDepthMask(true);
////        simBatch.flush();
////        simBatch.end();
////        Gdx.gl.glFlush();
////        Gdx.gl.glFinish();
//		 Gdx.gl.glDepthMask(false);
//		 //start the textureShader before setting any uniforms
//         textureShader.begin();
//       //update the projection matrix so our triangles are rendered in 2D
//         textureShader.setUniformMatrix("u_projTrans", cam.combined);
//         textureShader.setUniformi("u_texture", 0);
//         int vertexCount;
//         for(TileMesh m : meshes){
//
//        	 if(m.texture != null)
//        		 m.texture.bind();
//        	 vertexCount = m.getVertexCount();
//        	//render the mesh
//        	 m.mesh.render(textureShader, GL20.GL_TRIANGLES, 0, vertexCount);
//         }
//         textureShader.end();
//         //re-enable depth to reset states to their default
//         Gdx.gl.glDepthMask(true);
	 }
	        
}
