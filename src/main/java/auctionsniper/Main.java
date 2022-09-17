package auctionsniper;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import auctionsniper.ui.MainWindow;
import auctionsniper.xmpp.XMPPAuctionHouse;

public class Main {
  private static final int ARG_HOSTNAME = 0;
  private static final int ARG_USERNAME = 1;
  private static final int ARG_PASSWORD = 2;

  private final SniperPortfolio portfolio = new SniperPortfolio();

  private MainWindow ui;

  public Main() throws UserInterfaceStartupFailureException {
    startUserInterface();
  }

  public static void main(String... args) throws Exception {
    Main main = new Main();
    XMPPAuctionHouse auctionHouse = XMPPAuctionHouse.connect(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
    main.disconnectWhenUICloses(auctionHouse);
    main.addUserRequestListenerFor(auctionHouse);
  }

  private void startUserInterface() throws UserInterfaceStartupFailureException {
    try {
      SwingUtilities.invokeAndWait(() -> ui = new MainWindow(portfolio));
    }
    catch (InterruptedException | InvocationTargetException ex) {
      throw new UserInterfaceStartupFailureException(ex);
    }
  }

  private void disconnectWhenUICloses(final XMPPAuctionHouse auctionHouse) {
    ui.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        auctionHouse.disconnect();
      }
    });
  }

  private void addUserRequestListenerFor(final AuctionHouse auctionHouse) {
    ui.addUserRequestListener(new SniperLauncher(auctionHouse, portfolio));
  }
}
