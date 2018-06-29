
def quad =ScriptingEngine.gitScriptRun(	"https://github.com/xaveagle/SpiderQuad.git", 
								"loadRobot.groovy", 
["https://github.com/xaveagle/SpiderQuad.git",
		"Bowler/SpiderQuad.xml"]);

def gameController = ScriptingEngine.gitScriptRun(
            "https://gist.github.com/e26c0d8ef7d5283ef44fb22441a603b8.git", // git location of the library
            "LoadGameController.groovy" , // file to load
            // Parameters passed to the function
            ["GameController*"]
            )
         
if(gameController==null){

	return 
}

byte [] data = gameController.getData() 
double toSeconds=0.03//100 ms for each increment

while (!Thread.interrupted()){
	Thread.sleep((long)(toSeconds*1000))
	double xdata = data[4]
	double rzdata = data[3]
	double rxdata = data[1]
	double rydata = data[2]
	if(xdata<0)
		xdata+=256
	if(rzdata<0)
		rzdata+=256
	if(rxdata<0)
		rxdata+=256
	if(rydata<0)
		rydata+=256
	double scale = 1.0
	double displacement = 15*(scale*xdata/255.0-scale/2)
	double rot =((scale*rzdata/255.0)-scale/2)*-2.5
	double rotx =((rxdata/255.0)-scale/2)*5
	double roty =((rydata/255.0)-scale/2)*-5
	if(Math.abs(displacement)<0.1 ){
		displacement=0
	}
	if( Math.abs(rot)<0.1){
		rot=0
	}
	try{
	if(Math.abs(rotx)>0.1 || Math.abs(roty)>0.1){
		TransformNR move = new TransformNR(displacement,0,0,new RotationNR(rotx,0,roty))
		quad.getWalkingDriveEngine().pose(move)
	}
	}catch(Throwable t){}
	if(Math.abs(displacement)>0.1 || Math.abs(rot)>0.1){
		println "displacement "+displacement+" rot "+rot
		println "tilt "+rotx+" rot "+roty
		
		TransformNR move = new TransformNR(displacement,0,0,new RotationNR(rotx,rot,roty))
		quad.DriveArc(move, toSeconds);
	}
	
}
