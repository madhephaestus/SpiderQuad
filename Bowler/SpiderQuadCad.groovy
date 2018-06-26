import com.neuronrobotics.bowlerstudio.creature.ICadGenerator;
import com.neuronrobotics.bowlerstudio.creature.CreatureLab;
import org.apache.commons.io.IOUtils;
import com.neuronrobotics.bowlerstudio.vitamins.*;
import java.nio.file.Paths;
import eu.mihosoft.vrl.v3d.FileUtil;
import eu.mihosoft.vrl.v3d.Transform;
import javafx.scene.transform.Affine;
import com.neuronrobotics.bowlerstudio.physics.TransformFactory;
println "Loading STL file"
// Load an STL file from a git repo
// Loading a local file also works here

return new ICadGenerator(){
	
	private CSG moveDHValues(CSG incoming,DHLink dh ){
		TransformNR step = new TransformNR(dh.DhStep(0)).inverse()
		Transform move = TransformFactory.nrToCSG(step)
		return incoming.transformed(move)
		
	}

	@Override 
	public ArrayList<CSG> generateCad(DHParameterKinematics d, int linkIndex) {
		ArrayList<CSG> allCad=new ArrayList<>();
		String limbName = d.getScriptingName()
		File legFiles = null
		File covers = null
		boolean mirror=true
		if(limbName.contains("Left"){
			println "Mirror leg parts"
			mirror=false
		}
		TransformNR  legRoot= d.getRobotToFiducialTransform()
		def leftSide=false
		def rear = true
		if(legRoot.getY()>0){
			leftSide=true;
		}
		if(legRoot.getX()>0){
			rear=false;
		}
	
		if(leftSide){
			if(linkIndex ==0){
				legFiles = ScriptingEngine.fileFromGit(
				"https://github.com/xaveagle/SpiderQuad.git",
				"STLs/Shoulder.stl");
				covers = ScriptingEngine.fileFromGit(
				"https://github.com/xaveagle/SpiderQuad.git",
				"STLs/Shoulder Cover.stl");
	
			}
			if(linkIndex ==1){
				legFiles = ScriptingEngine.fileFromGit(
				"https://github.com/xaveagle/SpiderQuad.git",
				"STLs/Leg.stl");
				covers = ScriptingEngine.fileFromGit(
				"https://github.com/xaveagle/SpiderQuad.git",
				"STLs/Leg Cover.stl");
	
			}
	
			if(linkIndex ==2){
				legFiles = ScriptingEngine.fileFromGit(
				"https://github.com/xaveagle/SpiderQuad.git",
				"STLs/Foot.stl");
			}
		}
		else{
			if(linkIndex ==0){
				legFiles = ScriptingEngine.fileFromGit(
				"https://github.com/xaveagle/SpiderQuad.git",
				"STLs/Shoulder Mirror.stl");
	
			}
			if(linkIndex ==1){
				legFiles = ScriptingEngine.fileFromGit(
				"https://github.com/xaveagle/SpiderQuad.git",
				"STLs/Leg Mirror.stl");
	
			}
	
			if(linkIndex ==2){
				legFiles = ScriptingEngine.fileFromGit(
				"https://github.com/xaveagle/SpiderQuad.git",
				"STLs/Foot Mirror.stl");
	
			}
		}


		ArrayList<DHLink> dhLinks = d.getChain().getLinks()
		DHLink dh = dhLinks.get(linkIndex)
		// Hardware to engineering units configuration
		LinkConfiguration conf = d.getLinkConfiguration(linkIndex);
		// Engineering units to kinematics link (limits and hardware type abstraction)
		AbstractLink abstractLink = d.getAbstractLink(linkIndex);// Transform used by the UI to render the location of the object
		// Transform used by the UI to render the location of the object
		Affine manipulator = dh.getListener();

		
		// Load the .CSG from the disk and cache it in memory
		println "Loading " +legFiles
		CSG body  = Vitamins.get(legFiles)
		CSG body2
		  
		if(covers != null){
			body2 = Vitamins.get(covers)
		}
		if(linkIndex ==0){
			//body=moveDHValues(body,dh)
				body=body.rotx(180)
				//.rotx(180)
				//if(rear)
					//body=body.rotx(180)
					if(covers != null){
						body2=body2.rotx(180)
					}
				
		}
		if(linkIndex ==1){
			//body=body.roty(180)
			}
		
		if(linkIndex ==2){
			//body=body.roty(180)
		}
		
		body.setManipulator(manipulator);
		if(covers != null)
		body2.setManipulator(manipulator);
		
		def parts = [body] as ArrayList<CSG>
		
		if(covers != null){
		parts.add(body2)
		}
		
		for(int i=0;i<parts.size();i++){
			parts.get(i).setColor(javafx.scene.paint.Color.RED)
		}
		return parts;
		
	}
	@Override 
	public ArrayList<CSG> generateBody(MobileBase b ) {
		ArrayList<CSG> allCad=new ArrayList<>();

		File mainBodyFile = ScriptingEngine.fileFromGit(
			"https://github.com/xaveagle/SpiderQuad.git",
			"STLs/Body.stl");

		// Load the .CSG from the disk and cache it in memory
		CSG body  = Vitamins.get(mainBodyFile)

		body.setManipulator(b.getRootListener());
		body.setColor(javafx.scene.paint.Color.WHITE)
		def parts = [body ] as ArrayList<CSG>
		for(int i=0;i<parts.size();i++){
			parts.get(i).setColor(javafx.scene.paint.Color.GRAY)
		}
		return parts;
	}
};
