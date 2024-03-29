package com.example.scenebuilderrepo;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

class MapTile extends StackPane
{

	Hexagon hex;
	ImageView hexColorBase = new ImageView();
	ImageView hexStateRing = new ImageView();
	HexImages hexStateRings;
	MapObject obj = null;
	boolean isClicked = false;
	boolean isHighlighted = false;
	private Player owner;

	MapTile (HexImages rings)
	{
		setHexStateRings(rings);
		setHexStateRing(rings.unclicked);
		this.getChildren().add(hexStateRing);
		this.getChildren().add(hexColorBase);
		hexColorBase.setFitHeight(GameInfo.hexsize);
		hexColorBase.setFitWidth(GameInfo.hexsize);
		hexStateRing.setFitHeight(GameInfo.hexsize);
		hexStateRing.setFitWidth(GameInfo.hexsize);
		this.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public
			void handle (MouseEvent mouseEvent)
			{
				if ( owner != null )
				{
					System.out.println("Hex belongs to player " + GameInfo.getPlayerId(owner.faction.id));
					System.out.println("Hexes owned " + owner.ownedHexes);
					if ( obj != null )
					{
						System.out.println("Unit AP: " + obj.getAction_points_cur() + "/" + obj.getAction_points_max());
					}
				}

				if ( isClicked )
				{
					Board.unClickAll();
					setHexRingToUnclicked();
					isClicked = false;
				}
				else
				{

					moveObjIfDestSelected();
					Board.unClickAll();
					//owner of the tile == current player
					if ( owner != null && owner.faction.id ==
										  GameInfo.playerFactions.get(GameInfo.currentPlayerCounter).id )
					{
						setHexRingToClicked();
						Board.selectTile(hex.x,
										 hex.y);
						isClicked = true;
					}
					if ( obj != null )
					{

						if ( GameInfo.getPlayerId(owner.faction.id) == GameInfo.currentPlayerCounter &&
							 obj.getAction_points_cur() != 0 )
						{
							Board.selectTileAndHighlightNearby(hex.x,
															   hex.y);
						}
						isClicked = true;
						setHexRingToClicked();
						setPortrait();
					}
				}
			}
		});
	}

	void setPortrait()
	{
		hex.controller.setUnitPortraitAndDesc(this);
	}

	void setHexRingToClicked ()
	{
		hexStateRing.setImage(hexStateRings.clicked);
	}

	void setHexRingToHighlighted ()
	{
		hexStateRing.setImage(hexStateRings.highlighted);
		isHighlighted = true;
	}

	void setHexRingToUnclicked ()
	{
		hexStateRing.setImage(hexStateRings.unclicked);
		isClicked = false;
		this.isHighlighted = false;
	}

	void setHexStateRing (Image _ring)
	{
		hexStateRing.setImage(_ring);
	}

	void setHexStateRings (HexImages _rings)
	{
		hexStateRings = _rings;
	}

	void setHexColorBase (Image _base)
	{
		hexColorBase.setImage(_base);
	}

	void addMapObject (MapObject x)
	{
		obj = x;
		getChildren().add(x);
	}

	Player getOwner ()
	{
		return owner;
	}

	public
	void setOwner (Player _owner)
	{
		if ( _owner == null )
		{
			return;
		}

		if ( owner != null )
		{
			owner.ownedHexes--;
			owner.setIncome();
		}
		owner = _owner;
		owner.ownedHexes++;
		owner.setIncome();
		hex.controller.setFactionGold(owner);
		setHexColorBase(owner.faction.color);
	}

	void moveObjIfDestSelected ()
	{
		if ( isHighlighted )
		{
			Board.unitMove(this);
		}
	}

	public int getTerrainDef() {
		if(hex.terrain==null) return 0;
		return getOwner().faction.getTerrainDef(hex.terrain.getID());
	}

	public int getTerrainAtk() {
		if(hex.terrain==null) return 0;
		return getOwner().faction.getTerrainAtk(hex.terrain.getID());
	}
}

class Hexagon extends ImageView
{
	//default images
	int x;
	int y;
	GameController controller;
	TerrainEnum terrain;


	Hexagon (int x, int y, Player _onwer, GameController controller)
	{
		this.x = x;
		this.y = y;
		this.controller = controller;

	}

	public void setTerrain(TerrainEnum terrain) {
		this.terrain = terrain;
	}
}

class MapObject extends ImageView
{
	Image portriat;
	Faction faction;
	MapTile parent;
	Image attacker;
	Image attackAnimation;

	int def;
	int atk;
	private int hp_current;
	private int hp_max;
	private int action_points_cur;
	private int action_points_max;

	public
	int getHp_current ()
	{
		return hp_current;
	}

	public
	void setHp_current (int hp_current)
	{
		this.hp_current = hp_current;
	}

	public
	int getHp_max ()
	{
		return hp_max;
	}

	public
	void setHp_max (int hp_max)
	{
		this.hp_max = hp_max;
	}

	public
	int getAction_points_cur ()
	{
		return action_points_cur;
	}

	public
	void setAction_points_cur (int action_points_cur)
	{
		this.action_points_cur = action_points_cur;
	}

	public
	int getAction_points_max ()
	{
		return action_points_max;
	}

	public
	void setAction_points_max (int action_points_max)
	{
		this.action_points_max = action_points_max;
	}
}

class Unit extends MapObject
{


	public
	Unit (Faction _faction, Image _portriat, Image _attacker, Image _attackAnimation)
	{
		if ( _faction.id == FactionEnum.CRYSTALMEN )
		{
			setImage(new Image(new File("CRYSTAL_UNIT.png")
									   .toURI()
									   .toString()));
		}
		if ( _faction.id == FactionEnum.FORESTMEN )
		{
			setImage(new Image(new File("FOREST_UNIT.png")
									   .toURI()
									   .toString()));
		}
		if ( _faction.id == FactionEnum.SKYMEN )
		{
			setImage(new Image(new File("FLYING_UNIT.png")
									   .toURI()
									   .toString()));
		}
		this.faction = _faction;
		this.portriat = _portriat;
		this.attacker=_attacker;
		this.attackAnimation=_attackAnimation;

		atk = 37;
		def = 6;
		setHp_current(20);
		setHp_max(20);
		setAction_points_cur(1);
		setAction_points_max(1);
	}
}

class HQ extends MapObject
{

	public
	HQ (Faction _faction, Image _portriat)
	{
		if ( _faction.id == FactionEnum.CRYSTALMEN )
		{
			setImage(new Image(new File("CRYSTAL_HQ.png")
									   .toURI()
									   .toString()));
		}
		if ( _faction.id == FactionEnum.FORESTMEN )
		{
			setImage(new Image(new File("FOREST_HQ.png")
									   .toURI()
									   .toString()));
		}
		if ( _faction.id == FactionEnum.SKYMEN )
		{
			setImage(new Image(new File("FLYING_HQ.png")
									   .toURI()
									   .toString()));
		}
		this.faction = _faction;
		this.portriat = _portriat;

		def = 5;
		atk = 0;
		setHp_current(95);
		setHp_max(100);
		setAction_points_cur(0);
		setAction_points_max(0);
	}
}

class Player
{
	public int ownedHexes = 0;
	float income = 1;
	float gold = 5;
	Faction faction;


	public
	Player (Faction _Faction)
	{
		faction = _Faction;
	}

	void setIncome ()
	{
		income = (float) (1 + (ownedHexes * 0.2));
	}
}


class Board
{
	static int width;
	static int height;
	static MapTile selectedTile = null;

	static ArrayList<Vector<MapTile>> mapTiles = new ArrayList<>();

	public static
	ArrayList<Vector<MapTile>> getAllMapTiles ()
	{
		return mapTiles;
	}

	static
	void unClickAll ()
	{
		for (int i = 0; i < mapTiles.size(); i++)
		{
			for (int j = 0; j < mapTiles.get(i).size(); j++)
			{
				mapTiles.get(i).get(j).setHexRingToUnclicked();
			}
		}
		selectedTile = null;
	}

	static
	void selectTile (int x, int y)
	{
		selectedTile = mapTiles.get(x).get(y);
	}

	static
	void selectTileAndHighlightNearby (int x, int y)
	{
		selectTile(x,
				   y);
		// The lookup tables are in a {x,y} format
		int[][] ODD_COLUMN_LOOKUP_TABLE = {{1, 1}, {1, 0}, {0, -1}, {-1, 0}, {-1, 1}, {0, 1}};
		int[][] EVEN_COLUMN_LOOKUP_TABLE = {{1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {0, 1}};
		if ( !selectedTile.obj.getClass().equals(Unit.class) )
		{
			return;
		}
		if ( x % 2 == 0 )
		{ //Highlighting neighbouring mapTiles. Even column version.
			for (int i = 0; i < EVEN_COLUMN_LOOKUP_TABLE.length; i++)
			{
				int x_offset = EVEN_COLUMN_LOOKUP_TABLE[i][0];
				int y_offset = EVEN_COLUMN_LOOKUP_TABLE[i][1];
				if ( (x + x_offset >= 0 && x + x_offset < MapConstants.MAP_LENGTH) &&
					 (y + y_offset >= 0 && y + y_offset < MapConstants.MAP_HEIGHT) )
				{
					mapTiles.get(x + x_offset).get(y + y_offset).setHexRingToHighlighted();
				}
			}
		}
		else        //Highlighting neighbouring mapTiles. Odd column version
		{
			for (int i = 0; i < ODD_COLUMN_LOOKUP_TABLE.length; i++)
			{
				int x_offset = ODD_COLUMN_LOOKUP_TABLE[i][0];
				int y_offset = ODD_COLUMN_LOOKUP_TABLE[i][1];
				if ( (x + x_offset >= 0 && x + x_offset < MapConstants.MAP_LENGTH) &&
					 (y + y_offset >= 0 && y + y_offset < MapConstants.MAP_HEIGHT) )
				{
					mapTiles.get(x + x_offset).get(y + y_offset).setHexRingToHighlighted();
				}
			}
		}
	}

	@FXML
	static
	void unitMove (MapTile destinationTile)
	{
		if ( destinationTile.obj == null && selectedTile.obj.getClass().equals(Unit.class) )
		{
			//assigning the obj from the previous mapTile to the destination tile
			destinationTile.obj = selectedTile.obj;
			destinationTile.obj.setAction_points_cur(destinationTile.obj.getAction_points_cur() - 1);
			destinationTile.getChildren().add(destinationTile.obj);
			destinationTile.setOwner(selectedTile.getOwner());
			//removing the obj from the previous mapTile
			selectedTile.getChildren().remove(selectedTile.obj);
			selectedTile.obj = null;
		}
		else if ( (destinationTile.obj.getClass().equals(Unit.class) ||
				  destinationTile.obj.getClass().equals(HQ.class)) &&
				  destinationTile.getOwner() != selectedTile.getOwner() )
		{
			destinationTile.hex.controller.doAttack(selectedTile,destinationTile);
			battleCalc(destinationTile);
		}
	}

	//Function that calculates how
	static
	void battleCalc (MapTile destinationTile)
	{
		Random rand = new Random();
		int max = 1;
		int min = -1;
		int attackerHPbefore = selectedTile.obj.getHp_current();
		int attackerHPafterBase = attackerHPbefore - destinationTile.obj.def;
		int attackerHPafterTerrainBonus = destinationTile.getTerrainDef();
		int attackerHPafterRand = rand.nextInt((max - min) + 1) + min;

		int attackerHPafter = attackerHPafterBase - attackerHPafterTerrainBonus - attackerHPafterRand;

		int defenderHPbefore = destinationTile.obj.getHp_current();
		int defenderHPafterBase = defenderHPbefore - selectedTile.obj.atk;
		int defenderHPafterRand = rand.nextInt((max - min) + 1) + min;
		int defenderHPafterTerrainBonus = selectedTile.getTerrainAtk();

		int defenderHPafter = defenderHPafterBase - defenderHPafterTerrainBonus - defenderHPafterRand;

		if ( attackerHPafter <= 0 )
		{
			selectedTile.obj.setImage(null);
			selectedTile.obj.portriat = null;
			if ( selectedTile.obj.getClass() == Unit.class )
			{
				GameInfo.removeUnit(selectedTile);
			}
			else
			{
				removeHQ(selectedTile.hex.x, selectedTile.hex.y);
				GameInfo.removeHQ(selectedTile);
			}
			selectedTile.obj = null;
			return;
		}

		selectedTile.obj.setHp_current(attackerHPafter);
		if ( defenderHPafter <= 0 )
		{
			destinationTile.obj.setImage(null);
			destinationTile.obj.portriat = null;
			if ( destinationTile.obj.getClass() == Unit.class )
			{
				GameInfo.removeUnit(destinationTile);
			}
			else
			{
				removeHQ(destinationTile.hex.x,
						 destinationTile.hex.y);
				GameInfo.removeHQ(destinationTile);
			}
			destinationTile.obj = null;
			return;
		}

		destinationTile.obj.setHp_current(defenderHPafter);
	}

	static
	void addUnit (Faction faction, int i, int j)
	{
		if ( faction == null )
		{
			return;
		}

		mapTiles.get(i).get(j).setOwner(faction.pl);
		mapTiles.get(i).get(j).setHexColorBase(faction.color);
		Unit unit;
		if ( faction.id == FactionEnum.SKYMEN )
		{
			unit = new Unit(faction, new Image(new File("FLYING_UNIT_PORTRAIT.png").toURI().toString()),
									 new Image(new File("attack_flying.png").toURI().toString()),
									 new Image(new File("flying_attack_animation.gif").toURI().toString()));
		}
		else if ( faction.id == FactionEnum.CRYSTALMEN )
		{
			unit = new Unit(faction, new Image(new File("CRYSTAL_UNIT_PORTRAIT.png").toURI().toString()),
									 new Image(new File("attack_crystal.png").toURI().toString()),
									 new Image(new File("crystal_attack_animation.gif").toURI().toString()));
		}
		else
		{
			unit = new Unit(faction, new Image(new File("TREE_UNIT_PORTRAIT.png").toURI().toString()),
							  		 new Image(new File("attack_tree.png").toURI().toString()),
									 new Image(new File("tree_attack_animation.gif").toURI().toString()));
		}
		unit.setFitWidth(GameInfo.hexsize);
		unit.setFitHeight(GameInfo.hexsize);
		mapTiles.get(i).get(j).addMapObject(unit);
		GameInfo.addUnit(faction.id, unit);
	}

	static
	void addHQ (Faction faction, int i, int j)
	{
		if ( faction == null )
		{
			return;
		}
		mapTiles.get(i).get(j).hex.setImage(new Image(new File("hexagon.png").toURI().toString()));
		mapTiles.get(i).get(j).setOwner(faction.pl);
		mapTiles.get(i).get(j).setHexColorBase(faction.color);
		HQ hq;
		if ( faction.id == FactionEnum.SKYMEN )
		{
			hq = new HQ(faction, new Image(new File("flying_meteor.png").toURI().toString()));
		}
		else if ( faction.id == FactionEnum.CRYSTALMEN )
		{
			hq = new HQ(faction, new Image(new File("crystal_meteor.png").toURI().toString()));
		}
		else
		{
			hq = new HQ(faction, new Image(new File("tree_meteor.png").toURI().toString()));
		}
		hq.setFitWidth(GameInfo.hexsize);
		hq.setFitHeight(GameInfo.hexsize);
		mapTiles.get(i).get(j).addMapObject(hq);
		GameInfo.addHQ(faction.id, hq);
	}

	static
	void removeHQ (int i, int j)
	{
		Faction faction = mapTiles.get(i).get(j).obj.faction;
		for (int k = 0; k < mapTiles.size(); k++)
		{
			for (int l = 0; l < mapTiles.get(k).size(); l++)
			{
				if ( mapTiles.get(k).get(l).hexColorBase.getImage() == faction.color )
				{
					mapTiles.get(k).get(l).setHexColorBase(null);
				}
				if ( mapTiles.get(k).get(l).obj != null )
				{
					if ( mapTiles.get(k).get(l).obj.faction == faction )
					{
						mapTiles.get(k).get(l).obj.setImage(null);
						mapTiles.get(k).get(l).obj.portriat = null;
					}
				}
			}
		}
		GameInfo.removeHQ(mapTiles.get(i).get(j));

	}

	static
	MapTile findMapTile (MapObject object)
	{
		for (int i = 0; i < mapTiles.size(); i++)
		{
			for (int j = 0; j < mapTiles.get(i).size();j++)
			{
				if ( object == mapTiles.get(i).get(j).obj )
				{
					return mapTiles.get(i).get(j);
				}
			}
		}
		return null;
	}

	void addMapTile (MapTile tile, int x)
	{
		mapTiles.get(x).add(tile);
	}

	void addColumn ()
	{
		Vector<MapTile> hexColumn = new Vector<>();
		mapTiles.add(hexColumn);
	}
}

class Faction
{
	Image color;

	Player pl;

	FactionEnum id;

	TerrainEffects effects=new TerrainEffects();

	public
	Faction (FactionEnum _id, Image _color)
	{
		id = _id;
		if(_id==FactionEnum.CRYSTALMEN)
		{
			effects.setAtk(0,0,0,0,0);
			effects.setDef(0,0,0,0,6);
		}
		else if(_id==FactionEnum.FORESTMEN)
		{
			effects.setAtk(-2,0,0,2,0);
			effects.setDef(-3,0,0,2,0);
		}
		else if(_id==FactionEnum.SKYMEN)
		{
			effects.setAtk(-2,4,2,0,0);
			effects.setDef(-3,4,2,0,0);
		}
		color = _color;
	}

	public void setEffects(TerrainEffects effects) {
		this.effects = effects;
	}

	public int getTerrainDef(int x) {
		return effects.def.get(x);
	}
	public int getTerrainAtk(int x) {
		return effects.atk.get(x);
	}
}

class TerrainEffects
{
	Vector<Integer> atk = new Vector<Integer>();
	Vector<Integer> def = new Vector<Integer>();
	Vector<TerrainEnum> terrain = new Vector<TerrainEnum>();

	public TerrainEffects()
	{
		for(int i=0;i<5;i++) {
			terrain.add(TerrainEnum.getEnumById(i));
		}
	}

	public void setAtk(int radioactiveBonus,int mountainBonus,int hillBonus, int forestBonus, int ruinsBonus)
	{
		atk.add(radioactiveBonus);
		atk.add(mountainBonus);
		atk.add(hillBonus);
		atk.add(forestBonus);
		atk.add(ruinsBonus);
	}
	public void setDef(int radioactiveBonus,int mountainBonus,int hillBonus, int forestBonus, int ruinsBonus)
	{
		def.add(radioactiveBonus);
		def.add(mountainBonus);
		def.add(hillBonus);
		def.add(forestBonus);
		def.add(ruinsBonus);
	}

}