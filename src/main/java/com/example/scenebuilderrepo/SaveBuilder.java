package com.example.scenebuilderrepo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

public
class SaveBuilder
{
	ArrayList<Vector<MapTile>> mapTiles = Board.getAllMapTiles();
	String savenamePath;

	public
	SaveBuilder (String _savenamePath)
	{
		this.savenamePath = _savenamePath;
	}

	public static
	String parseFilename (String filename)
	{
		int beginning = 0;
		for (int i = filename.length() - 1; ; i--)
		{
			if ( filename.charAt(i) == '/' )
			{
				beginning = i + 1;
				break;
			}
		}

		return filename.substring(beginning);
	}

	public
	void saveGameToXML ()
	{
		try
		{
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

			Document document = documentBuilder.newDocument();

			//root element
			Element root = document.createElement("game");
			document.appendChild(root);

			//game info
			Element gameInfo = document.createElement("gameInfo");
			root.appendChild(gameInfo);

			//player amount
			Element playerAmount = document.createElement("playerAmount");
			playerAmount.appendChild(document.createTextNode(Integer.toString(GameInfo.playerAmount)));
			gameInfo.appendChild(playerAmount);
			//turn
			Element turn = document.createElement("turn");
			turn.appendChild(document.createTextNode(Integer.toString(GameInfo.turn)));
			gameInfo.appendChild(turn);
			//current player
			Element currentPlayer = document.createElement("currentPlayer");
			currentPlayer.appendChild(document.createTextNode(Integer.toString(GameInfo.currentPlayerCounter)));
			gameInfo.appendChild(currentPlayer);

			Element factionList = document.createElement("factionList");
			gameInfo.appendChild(factionList);

			for (int i = 0; i < GameInfo.playerFactions.size(); i++)
			{
				Element factionEnum = document.createElement("factionEnum");
				factionEnum.appendChild(document.createTextNode(GameInfo.playerFactions.get(i).id.name()));
				factionList.appendChild(factionEnum);
			}

			Element playerInformation = document.createElement("PlayerInformation");
			gameInfo.appendChild(playerInformation);

			for (int i = 0; i < GameInfo.playerFactions.size(); i++)
			{
				Element factionNumber = document.createElement("Player");
				playerInformation.appendChild(factionNumber);

				Element gold = document.createElement("gold");
				float gold_amount = GameInfo.playerFactions.get(i).pl.gold;
				gold.appendChild(document.createTextNode(Float.toString(gold_amount)));
				factionNumber.appendChild(gold);

				Element income = document.createElement("income");
				float income_amount = GameInfo.playerFactions.get(i).pl.income;
				income.appendChild(document.createTextNode(Float.toString(income_amount)));
				factionNumber.appendChild(income);

				Element ownedHexesEl = document.createElement("ownedHexes");
				int ownedHexes = GameInfo.playerFactions.get(i).pl.ownedHexes;
				ownedHexesEl.appendChild(document.createTextNode(Integer.toString(ownedHexes)));
				factionNumber.appendChild(ownedHexesEl);
			}

			//board information
			Element board = document.createElement("board");
			root.appendChild(board);

			for (int i = 0; i < mapTiles.size(); i++)
			{
				for (
						int j = 0;
						j < mapTiles
								.get(i)
								.size();
						j++)
				{
					MapTile tile = mapTiles
							.get(i)
							.get(j);

					Element hex = document.createElement("hex");
					board.appendChild(hex);

					Element x = document.createElement("x");
					x.appendChild(document.createTextNode(Integer.toString(i)));
					hex.appendChild(x);
					Element y = document.createElement("y");
					y.appendChild(document.createTextNode(Integer.toString(j)));
					hex.appendChild(y);

					Element object = document.createElement("obj");
					if ( tile.obj == null )
					{
						object.appendChild(document.createTextNode("null"));
					}
					else if ( tile.obj.getClass() == HQ.class )
					{
						object.appendChild(document.createTextNode("HQ"));
					}
					else
					{
						object.appendChild(document.createTextNode("Unit"));
					}
					hex.appendChild(object);

					Element actionPointsMax = document.createElement("actionPointsMax");
					if ( tile.obj != null )
					{
						actionPointsMax.appendChild(
								document.createTextNode(Integer.toString(tile.obj.getAction_points_max())));
					}
					else
					{
						actionPointsMax.appendChild(document.createTextNode(Integer.toString(0)));
					}
					hex.appendChild(actionPointsMax);

					Element actionPointsCurr = document.createElement("actionPointsCurr");
					if ( tile.obj != null )
					{
						actionPointsCurr.appendChild(
								document.createTextNode(Integer.toString(tile.obj.getAction_points_cur())));
					}
					else
					{
						actionPointsCurr.appendChild(document.createTextNode(Integer.toString(0)));
					}
					hex.appendChild(actionPointsCurr);

					Element hpCurrent = document.createElement("hpCurrent");
					if ( tile.obj != null )
					{
						hpCurrent.appendChild(document.createTextNode(Integer.toString(tile.obj.getHp_current())));
					}
					else
					{
						hpCurrent.appendChild(document.createTextNode(Integer.toString(0)));
					}
					hex.appendChild(hpCurrent);

					Element hpMax = document.createElement("hpMax");
					if ( tile.obj != null )
					{
						hpMax.appendChild(document.createTextNode(Integer.toString(tile.obj.getHp_max())));
					}
					else
					{
						hpMax.appendChild(document.createTextNode(Integer.toString(0)));
					}
					hex.appendChild(hpMax);

					Element defense = document.createElement("defense");
					if ( tile.obj != null )
					{
						defense.appendChild(document.createTextNode(Integer.toString(tile.obj.def)));
					}
					else
					{
						defense.appendChild(document.createTextNode(Integer.toString(0)));
					}
					hex.appendChild(defense);

					Element attack = document.createElement("attack");
					if ( tile.obj != null )
					{
						attack.appendChild(document.createTextNode(Integer.toString(tile.obj.atk)));
					}
					else
					{
						attack.appendChild(document.createTextNode(Integer.toString(0)));
					}
					hex.appendChild(attack);

					Element tileOwner = document.createElement("tileOwner");
					if ( tile.getOwner() == null )
					{
						tileOwner.appendChild(document.createTextNode("null"));
					}
					else
					{
						tileOwner.appendChild(document.createTextNode(tile.getOwner().faction.id.name()));
					}
					hex.appendChild(tileOwner);

					Element imagefilename = document.createElement("imagefilename");
					if ( tile.hex.getImage() == null )
					{
						imagefilename.appendChild(document.createTextNode("hexagon.png"));
					}
					else
					{
						String filename = parseFilename(tile.hex
																.getImage()
																.getUrl());
						imagefilename.appendChild(document.createTextNode(filename));
					}
					hex.appendChild(imagefilename);


				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.INDENT,
										  "yes");

			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File("save.xml"));

			transformer.transform(domSource,
								  streamResult);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
