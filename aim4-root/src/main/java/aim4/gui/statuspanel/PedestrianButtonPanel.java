 package aim4.gui.statuspanel;
import aim4.config.Debug;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import aim4.gui.StatusPanelInterface;
import aim4.gui.Viewer;
import aim4.im.IntersectionManager;
import aim4.im.v2i.RequestHandler.*;
import aim4.im.v2i.V2IManager;
import aim4.im.v2i.policy.BasePolicy;
import aim4.sim.Simulator;

public class PedestrianButtonPanel extends JPanel implements StatusPanelInterface, ActionListener { 
	
	private static final long serialVersionUID = 1L;
	
	Viewer viewer;
	
	// Six buttons to represent six paths across an intersection. 
	private JButton left;
	private JButton right;
	private JButton top;
	private JButton bottom;
	private JButton topLeftToBottomRight;
	private JButton topRightToBottomLeft;
        private JButton stopAll;
	
	public PedestrianButtonPanel(Viewer viewer) {
		
		left = new JButton("Left");
		right = new JButton("Right");
		top = new JButton("Top");
		bottom = new JButton("Bottom");
		topLeftToBottomRight = new JButton("Top Left to Bottom Right");
		topRightToBottomLeft = new JButton("Top Right to Bottom Left");
                stopAll = new JButton("All");
		
		this.viewer = viewer;
		
		// layout
		
	    GroupLayout layout = new GroupLayout(this);
	    setLayout(layout);
	    layout.setAutoCreateGaps(false);
	    layout.setAutoCreateContainerGaps(false);

	    layout.setHorizontalGroup(layout
	      .createSequentialGroup()
	        .addComponent(left)
	        .addComponent(right)
	        .addComponent(top)
	        .addComponent(bottom)
	        .addComponent(topLeftToBottomRight)
	        .addComponent(topRightToBottomLeft)
                .addComponent(stopAll)
	        
	    );

	    layout.setVerticalGroup(layout
	      .createParallelGroup(GroupLayout.Alignment.CENTER)
	      .addComponent(left)
	        .addComponent(right)
	        .addComponent(top)
	        .addComponent(bottom)
	        .addComponent(topLeftToBottomRight)
	        .addComponent(topRightToBottomLeft)
                .addComponent(stopAll)
	    );
	    
 
	    // add event handler
	    
	    
	    left.addActionListener(this);
	    right.addActionListener(this);
	    top.addActionListener(this);
	    bottom.addActionListener(this);
	    topLeftToBottomRight.addActionListener(this);
	    topRightToBottomLeft.addActionListener(this);
            stopAll.addActionListener(this);
		
	}
		
	
	// Event handler
        @Override
	public void actionPerformed(ActionEvent e) {
            int imId = Debug.getTargetIMid();
            if (imId >= 0) {
              Simulator sim = viewer.getSimulator();
              IntersectionManager im0 =
                sim.getMap().getIntersectionManagers().get(imId);
              assert im0.getId() == imId;
              if (im0 instanceof V2IManager) {
                V2IManager im = (V2IManager)im0;
                if (im.getPolicy() instanceof BasePolicy) {
                    BasePolicy policy = (BasePolicy)im.getPolicy();
                    if(policy.getRequestHandler() instanceof PedestrianRequestHandler || policy.getRequestHandler() instanceof AllStopRequestHandler){
                        
                        if (e.getSource() == left) {
                            policy.getPedestrianRequestHandler().setLeft();
                        }
                        if (e.getSource() == right) {
                            policy.getPedestrianRequestHandler().setRight();
                        }
                        if (e.getSource() == top) {
                            policy.getPedestrianRequestHandler().setTop();
                        }
                        if (e.getSource() == bottom) {
                            policy.getPedestrianRequestHandler().setBottom();
                        }
                        if (e.getSource() == topLeftToBottomRight) {
                            policy.getPedestrianRequestHandler().setTopLeftToBottomRight();
                        }
                        if (e.getSource() == topRightToBottomLeft) {
                            policy.getPedestrianRequestHandler().setTopRightToBottomLeft();
                        }
                        if (e.getSource() == stopAll) {
                            policy.getPedestrianRequestHandler().setStopAll();
                        }
                	
	}
              }
            }
        }
        }
               
	public void update() {
	    // do nothing
	  }
	
	public void clear() {
	    // do nothing
	  }

}

