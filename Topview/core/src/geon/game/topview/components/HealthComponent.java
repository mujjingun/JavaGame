package geon.game.topview.components;

import com.badlogic.ashley.core.Component;

/**
 * <pre>
 * 
 * 	|_HealthComponent
 * 
 * 개요 : 
 * 작성일 : Nov 17, 2015
 * </pre>
 *
 * @author	박건
 * @version	1.0
 */
public class HealthComponent implements Component {
	
	public int maxHealth = 100;
	public int health = 100;
}
