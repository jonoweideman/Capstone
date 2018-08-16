/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aim4.im.v2i.RequestHandler;

import java.util.Iterator;
import java.util.List;

import aim4.im.v2i.policy.BasePolicy;
import aim4.im.v2i.policy.BasePolicyCallback;
import aim4.im.v2i.policy.BasePolicy.ProposalFilterResult;
import aim4.im.v2i.policy.BasePolicy.ReserveParam;
import aim4.msg.i2v.Reject;
import aim4.msg.v2i.Request;
import aim4.sim.StatCollector;

/**
 *
 * @author Philip
 */
public class PedestrianRequestHandler implements RequestHandler{
    /////////////////////////////////
  // CONSTANTS
  /////////////////////////////////

  /** The switch time interval */
  private static final double SWITCH_TIME_INTERVAL = 20.0;

  /////////////////////////////////
  // PRIVATE FIELDS
  /////////////////////////////////

  /** The base policy */
  private BasePolicyCallback basePolicy;
  /** The next switch time */
  private double nextSwitchTime;
  /** Whether pedestrians are crossing the left road*/
  private boolean left;
  /** Whether pedestrians are crossing the right road*/
  private boolean right;
  /** Whether pedestrians are crossing the top road*/
  private boolean top;
  /** Whether pedestrians are crossing the bottom road*/
  private boolean bottom;
  /** Whether pedestrians are crossing the intersection diagonally from top left to bottom right road*/
  private boolean topLeftToBottomRight;
  /** Whether pedestrians are crossing the intersection diagonally from top right to bottom left road*/
  private boolean topRightToBottomLeft;
  /** Whether pedestrians are crossing all roads and across the intersection*/
  private boolean stopAll;

  /////////////////////////////////
  // PUBLIC METHODS
  /////////////////////////////////

  /**
   * Set the base policy call-back.
   *
   * @param basePolicy  the base policy's call-back
   */
  @Override
  public void setBasePolicyCallback(BasePolicyCallback basePolicy) {
    this.basePolicy = basePolicy;
    this.nextSwitchTime = basePolicy.getCurrentTime() + SWITCH_TIME_INTERVAL;
  }

  /**
   * Let the request handler to act for a given time period.
   *
   * @param timeStep  the time period
   */
  @Override
  public void act(double timeStep) {
    if (basePolicy.getCurrentTime() > nextSwitchTime) {

      nextSwitchTime = basePolicy.getCurrentTime() + SWITCH_TIME_INTERVAL;
    }
  }

  /**
   * Process the request message.
   *
   * @param msg the request message
   */
  @Override
  public void processRequestMsg(Request msg) {
    int vin = msg.getVin();

    // If the vehicle has got a reservation already, reject it.
    if (basePolicy.hasReservation(vin)) {
      basePolicy.sendRejectMsg(vin, msg.getRequestId(),
                               Reject.Reason.CONFIRMED_ANOTHER_REQUEST);
      return;
    }

    // filter the proposals
    BasePolicy.ProposalFilterResult filterResult =
      BasePolicy.standardProposalsFilter(msg.getProposals(),
                                         basePolicy.getCurrentTime());
    if (filterResult.isNoProposalLeft()) {
      basePolicy.sendRejectMsg(vin,
                               msg.getRequestId(),
                               filterResult.getReason());
    }

    List<Request.Proposal> proposals = filterResult.getProposals();

    // remove proposals that is not in the correct turn direction.
    removeProposalWithIncorrectTurnDirection(proposals);
    if (proposals.isEmpty()) {
      basePolicy.sendRejectMsg(vin, msg.getRequestId(),
                               Reject.Reason.NO_CLEAR_PATH);
      return;
    }

    // try to see if reservation is possible for the remaining proposals.
    BasePolicy.ReserveParam reserveParam = basePolicy.findReserveParam(msg, proposals);
    if (reserveParam != null) {
      basePolicy.sendComfirmMsg(msg.getRequestId(), reserveParam);
    } else {
      basePolicy.sendRejectMsg(vin, msg.getRequestId(),
                               Reject.Reason.NO_CLEAR_PATH);
    }
  }

  /**
   * Get the statistic collector.
   *
   * @return the statistic collector
   */
  @Override
  public StatCollector<?> getStatCollector() {
    return null;
  }

  /**
   * Remove proposals whose arrival time is prohibited from entering
   * the intersection according to {@code canEnterAtArrivalTime()}.
   *
   * @param proposals  a set of proposals
   */
  private void removeProposalWithIncorrectTurnDirection(
                                             List<Request.Proposal> proposals) {
    for (Iterator<Request.Proposal> tpIter = proposals.listIterator();
         tpIter.hasNext();) {
      Request.Proposal p = tpIter.next();
      if (topRightToBottomLeft ||topLeftToBottomRight) {
        if (p.getArrivalLaneID() == p.getDepartureLaneID()) {
          tpIter.remove();
        }
      } else {
        if (p.getArrivalLaneID() != p.getDepartureLaneID()) {
          //tpIter.remove();
        }
      }
    }
  }
  
  public void setLeft(){
      if(left=true)
        left=false;
      else
        left=true;
  }
  
  public void setRight(){
      if(right=true)
        right=false;
      else
        right=true;
  }
  
  public void setTop(){
      if(top=true)
        top=false;
      else
        top=true;
  }
  
  public void setBottom(){
      if(bottom=true)
        bottom=false;
      else
        bottom=true;
  }
  
  public void setTopLeftToBottomRight(){
      if(topLeftToBottomRight=true)
        topLeftToBottomRight=false;
      else
        topLeftToBottomRight=true;
  }
  
  public void setTopRightToBottomLeft(){
      if(topRightToBottomLeft=true)
        topRightToBottomLeft=false;
      else
        topRightToBottomLeft=true;
  }
  
  public void setStopAll(){
      if(stopAll=true)
        stopAll=false;
      else
        stopAll=true;
  }

}
