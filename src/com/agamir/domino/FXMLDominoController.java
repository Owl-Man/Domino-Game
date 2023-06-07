package com.agamir.domino;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class FXMLDominoController implements Initializable {

	private List<Domino> dominoes = new ArrayList<>();

	private String lString, rString;
	private boolean isAi = false;

	@FXML
	FlowPane top_panel;
	@FXML
	FlowPane bottom_panel;
	@FXML
	FlowPane center_panel1, center_panel2, center_panel3, center_panel4;

	private Alert comWinner = new Alert(AlertType.INFORMATION, "Соперник победил",
			ButtonType.FINISH);
	private Alert userWinner = new Alert(AlertType.INFORMATION, "Вы выиграли",
			ButtonType.FINISH);

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initData();
		Collections.shuffle(dominoes);
		int size = dominoes.size();
		for (int i = size - 1; i >= size - 7; i--) {
			Domino c = dominoes.remove(i);
			addv(bottom_panel, c);
		}
		size = dominoes.size();
		for (int i = size - 1; i >= size - 7; i--) {
			add(top_panel, dominoes.remove(i));
		}
	}

	public void addCenter(FlowPane panel, ImageView iv, boolean isLeft) {
		iv.rotateProperty().set(((Domino) iv.getUserData()).getRotate());
		iv.setFitHeight(50);
		iv.setFitWidth(25);
		if (isLeft) {
			panel.getChildren().add(0, iv);
		} else {
			panel.getChildren().add(panel.getChildren().size(), iv);

		}
		FlowPane.setMargin(iv, new Insets(0, 25, 0, 0));
	}

	public void add(FlowPane panel, Domino card) {
		ImageView iv = new ImageView("file:resources/img/0_0.png");
		iv.setFitHeight(60);
		iv.setFitWidth(30);
		iv.setUserData(card);
		panel.getChildren().add(iv);
		FlowPane.setMargin(iv, new Insets(0, 30, 0, 0));
	}

	public void ai() {

		while (shouldTake(top_panel) && dominoes.size() != 0) {
			takeCard(top_panel, true);
		}
		ObservableList<Node> children = top_panel.getChildren();

		while (dominoes.size() == 0 && shouldTake(bottom_panel)) {
			int size = children.size();
			for (int i = 0; i < size; i++) {
				ImageView iv = (ImageView) children.get(i);
				if (doLeft(top_panel, iv)) {
					return;
				}
				if (doRight(top_panel, iv)) {
					return;
				}
			}
		}
		int size = children.size();
		for (int i = 0; i < size; i++) {
			ImageView iv = (ImageView) children.get(i);
			if (doLeft(top_panel, iv)) {
				return;
			}
			if (doRight(top_panel, iv)) {
				return;
			}
		}
	}

	private boolean doLeft(FlowPane panel, ImageView iv) {
		Domino card = (Domino) iv.getUserData();
		boolean canAddLeft = card.isCanAddLeft(lString);
		if (canAddLeft) {
			lString = card.getLeft();
			FlowPane checkLeft = checkLeft();
			panel.getChildren().remove(iv);
			iv.setImage(new Image(card.getSrc()));
			iv.setOnMouseClicked(null);
			addCenter(checkLeft, iv, true);
			System.out.println("left " + lString);
		}
		isWin();
		return canAddLeft;
	}

	private boolean doRight(FlowPane panel, ImageView iv) {
		Domino card = (Domino) iv.getUserData();
		boolean canAddRight = card.isCanAddRight(rString);
		if (canAddRight) {
			rString = card.getRight();
			FlowPane checkright = checkright();
			panel.getChildren().remove(iv);
			iv.setImage(new Image(card.getSrc()));
			iv.setOnMouseClicked(null);
			addCenter(checkright, iv, false);
			System.out.println("right " + rString);
		}
		isWin();
		return canAddRight;
	}

	public void addv(FlowPane panel, Domino card) {
		ImageView iv = new ImageView(card.getSrc());
		iv.setFitHeight(60);
		iv.setFitWidth(30);
		iv.setUserData(card);
		panel.getChildren().add(iv);
		FlowPane.setMargin(iv, new Insets(0, 30, 0, 0));
		iv.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (center_panel1.getChildren().size() == 0) {
					lString = card.getLeft();
					rString = card.getRight();
					panel.getChildren().remove(iv);
					iv.setOnMouseClicked(null);
					addCenter(center_panel1, iv, true);
					isAi = true;
					ai();
					isAi = false;
					return;
				}

				while (shouldTake(bottom_panel) && dominoes.size() != 0) {
					takeCard(bottom_panel, false);
				}

				if (doLeft(panel, iv)) {
					isAi = true;
					ai();
					isAi = false;
					return;
				}

				if (doRight(panel, iv)) {
					isAi = true;
					ai();
					isAi = false;
					return;
				}

			}
		});
	}

	private boolean shouldTake(FlowPane panel) {
		ObservableList<Node> children = panel.getChildren();
		int size = children.size();
		for (int i = 0; i < size; i++) {
			ImageView iv = (ImageView) children.get(i);
			Domino card = (Domino) iv.getUserData();
			if (card.hasSame(lString, lString)) {
				return false;
			}
		}
		return true;
	}

	private void takeCard(FlowPane panel, boolean isAi) {

		if (dominoes.size() != 0) {
			if (!isAi) {
				addv(panel, dominoes.remove(0));
			} else {
				add(panel, dominoes.remove(0));
			}
		}
	}

	private FlowPane checkLeft() {
		ObservableList<Node> children1 = center_panel1.getChildren();
		ObservableList<Node> children3 = center_panel3.getChildren();
		if (children1.size() == children3.size()) {
			return center_panel1;
		} else if (children1.size() - 1 == children3.size()) {
			return center_panel3;
		}
		return null;
	}

	private FlowPane checkright() {
		ObservableList<Node> children2 = center_panel2.getChildren();
		ObservableList<Node> children4 = center_panel4.getChildren();
		if (children2.size() == children4.size()) {
			return center_panel4;
		} else if (children2.size() + 1 == children4.size()) {
			return center_panel2;
		}
		return null;
	}

	private boolean isWin() {
		System.out.println("win size t " + top_panel.getChildren().size()
				+ " b " + bottom_panel.getChildren().size());
		if (top_panel.getChildren().size() == 0) {
			comWinner.show();
			return true;
		}

		if (bottom_panel.getChildren().size() == 0) {
			userWinner.show();
			;
			return true;
		}

		if (dominoes.size() == 0) {
			boolean com = shouldTake(top_panel);
			boolean user = shouldTake(bottom_panel);
			System.out.println("win " + com + " " + user + " " + isAi);
			if (com && user) {
				if (isAi) {
					userWinner.show();
					;
				} else {
					comWinner.show();
					;
				}
				return true;
			}
		}
		return false;
	}

	private void initData() {
		dominoes.add(new Domino("file:resources/img/0_0.png", "0", "0"));
		dominoes.add(new Domino("file:resources/img/0_1.png", "0", "1"));
		dominoes.add(new Domino("file:resources/img/0_2.png", "0", "2"));
		dominoes.add(new Domino("file:resources/img/0_3.png", "0", "3"));
		dominoes.add(new Domino("file:resources/img/0_4.png", "4", "0"));
		dominoes.add(new Domino("file:resources/img/0_5.png", "5", "0"));
		dominoes.add(new Domino("file:resources/img/0_6.png", "6", "0"));

		dominoes.add(new Domino("file:resources/img/1_1.png", "1", "1"));
		dominoes.add(new Domino("file:resources/img/1_2.png", "1", "2"));
		dominoes.add(new Domino("file:resources/img/1_3.png", "1", "3"));
		dominoes.add(new Domino("file:resources/img/1_4.png", "4", "1"));
		dominoes.add(new Domino("file:resources/img/1_5.png", "5", "1"));
		dominoes.add(new Domino("file:resources/img/1_6.png", "6", "1"));

		dominoes.add(new Domino("file:resources/img/2_2.png", "2", "2"));
		dominoes.add(new Domino("file:resources/img/2_3.png", "2", "3"));
		dominoes.add(new Domino("file:resources/img/2_4.png", "4", "2"));
		dominoes.add(new Domino("file:resources/img/2_5.png", "5", "2"));
		dominoes.add(new Domino("file:resources/img/2_6.png", "6", "2"));

		dominoes.add(new Domino("file:resources/img/3_3.png", "3", "3"));
		dominoes.add(new Domino("file:resources/img/3_4.png", "4", "3"));
		dominoes.add(new Domino("file:resources/img/3_5.png", "5", "3"));
		dominoes.add(new Domino("file:resources/img/4_6.png", "6", "3"));

		dominoes.add(new Domino("file:resources/img/4_4.png", "4", "3"));
		dominoes.add(new Domino("file:resources/img/4_5.png", "5", "4"));
		dominoes.add(new Domino("file:resources/img/4_6.png", "6", "4"));

		dominoes.add(new Domino("file:resources/img/5_5.png", "5", "5"));
		dominoes.add(new Domino("file:resources/img/5_6.png", "6", "5"));

		dominoes.add(new Domino("file:resources/img/6_6.png", "6", "6"));
	}

}
