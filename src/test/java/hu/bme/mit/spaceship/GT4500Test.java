package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore primary; 
  private TorpedoStore secondary; 

  @BeforeEach
  public void init(){
    primary = mock(TorpedoStore.class, "primaryTorpedoStore"); 
    secondary = mock(TorpedoStore.class, "secondaryTorpedoStore");


    this.ship = new GT4500(primary, secondary);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(primary.isEmpty()).thenReturn(false); 
    when(primary.fire(1)).thenReturn(true);
    when(secondary.isEmpty()).thenReturn(false); 
    when(secondary.fire(1)).thenReturn(true);


    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(primary, times(1)).fire(1); 
    verify(secondary, times(0)).fire(1); 
    assertTrue(result);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(primary.isEmpty()).thenReturn(false); 
    when(primary.fire(1)).thenReturn(true);
    when(secondary.isEmpty()).thenReturn(false); 
    when(secondary.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(primary, times(1)).fire(1); 
    verify(secondary, times(1)).fire(1);
    assertTrue(result);
  }


  /**
  * Tries to fire the torpedo stores of the ship.
  *
  * @param firingMode how many torpedo bays to fire
  * 	SINGLE: fires only one of the bays.
  * 			- For the first time the primary store is fired.
  * 			- To give some cooling time to the torpedo stores, torpedo stores are fired alternating.
  * 			- But if the store next in line is empty, the ship tries to fire the other store.
  * 			- If the fired store reports a failure, the ship does not try to fire the other one.
  * 	ALL:	tries to fire both of the torpedo stores.
  *
  * @return whether at least one torpedo was fired successfully
  */

  @Test
  public void fireTorpedo_Single_No_Primary_Success(){
    // Arrange
    when(primary.isEmpty()).thenReturn(true); 
    when(primary.fire(1)).thenReturn(false);
    when(secondary.isEmpty()).thenReturn(false); 
    when(secondary.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(primary, times(0)).fire(1); 
    verify(secondary, times(1)).fire(1); 
    assertTrue(result);
  }

  @Test
  public void fireTorpedo_Single_Failure(){
    // Arrange
    when(primary.isEmpty()).thenReturn(true); 
    when(primary.fire(1)).thenReturn(false);
    when(secondary.isEmpty()).thenReturn(true); 
    when(secondary.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(primary, times(0)).fire(1); 
    verify(secondary, times(0)).fire(1);
    assertFalse(result);
  }

  @Test
  public void fireTorpedo_All_Failure(){
    // Arrange
    when(primary.isEmpty()).thenReturn(true); 
    when(primary.fire(1)).thenReturn(false);
    when(secondary.isEmpty()).thenReturn(true); 
    when(secondary.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(primary, times(0)).fire(1); 
    verify(secondary, times(0)).fire(1);
    assertFalse(result);
  }

  @Test
  public void fireTorpedo_Single_Both_Success(){
    // Arrange
    when(primary.isEmpty()).thenReturn(false); 
    when(primary.fire(1)).thenReturn(true);
    when(secondary.isEmpty()).thenReturn(false); 
    when(secondary.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    result = result &&  ship.fireTorpedo(FiringMode.SINGLE);
  
    // Assert
    verify(primary, times(1)).fire(1); 
    verify(secondary, times(1)).fire(1);
    assertTrue(result);
  }

  @Test
  public void fireTorpedo_Single_Primary_Twice_Success(){
    // Arrange
    when(primary.isEmpty()).thenReturn(false); 
    when(primary.fire(1)).thenReturn(true);
    when(secondary.isEmpty()).thenReturn(true); 
    when(secondary.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    result = result &&  ship.fireTorpedo(FiringMode.SINGLE);
  
    // Assert
    verify(primary, times(2)).fire(1); 
    verify(secondary, times(0)).fire(1);
    assertTrue(result);
  }

  @Test
  public void fireTorpedo_Single_Primary_Twice_Failure(){
    // Arrange
    when(primary.isEmpty()).thenReturn(false).thenReturn(true); 
    when(primary.fire(1)).thenReturn(true).thenReturn(false);
    when(secondary.isEmpty()).thenReturn(true); 
    when(secondary.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    result = result &&  ship.fireTorpedo(FiringMode.SINGLE);
  
    // Assert
    verify(primary, times(1)).fire(1); 
    verify(secondary, times(0)).fire(1);
    assertFalse(result);
  }


  @Test
  public void fireTorpedo_All_Only_Secondary_Success(){
    // Arrange
    when(primary.isEmpty()).thenReturn(true); 
    when(primary.fire(1)).thenReturn(false); 
    when(secondary.isEmpty()).thenReturn(false);
    when(secondary.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);
  
    // Assert
    verify(primary, times(0)).fire(1); 
    verify(secondary, times(1)).fire(1);
    assertTrue(result);
  }

}
