package scalalab.utils

/** A Domain specific language for making JMenus
  * @author  	david@mel David R. MacIver
  */

import javax.swing._;

class MenuBar extends JMenuBar with MenuHolder {

}

trait MenuHolder {
  def add(menu: JMenu): JMenu;

  class Menu(name: String) extends JMenu(name) with MenuHolder {
    MenuHolder.this.add(this);

    def add(menu: JMenu) = add(menu: JMenuItem).asInstanceOf[JMenu]

    import java.awt.event._;

    class Item(name: String) extends JMenuItem(name) {
      Menu.this.add(this);

      def does(performAction: => Unit) = {
        addActionListener(new ActionListener {
          def actionPerformed(e: ActionEvent) = performAction;
        });
        this
      }
    }

    class CheckboxItem(name: String) extends JCheckBoxMenuItem(name) {
      Menu.this.add(this);

      def toggles(toggle: Boolean => Unit) = {
        addActionListener(new ActionListener {
          def actionPerformed(e: ActionEvent) = toggle(isSelected);
        });
        this
      }

      def is(x: Boolean) = {
        setSelected(x);
        this
      }
    }

    implicit def item(name: String) = new Item(name);

    implicit def checkboxItem(name: String) = new CheckboxItem(name);

    def --- = add(new JSeparator());
  }

}

