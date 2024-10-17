package client;

import limebrokerage.trading.api.AckOptions;
import limebrokerage.trading.api.Callback;
import limebrokerage.trading.api.FillOptions;
import limebrokerage.trading.api.PositionEffect;
import limebrokerage.trading.api.Side;
import limebrokerage.trading.api.USOptionsSymbol;

public class MyCallback implements Callback {

    // Called when a previous fill on an US-Equities order is busted (reversed).
    public void onBust(long eventId, long orderId, Side reversedSide, long bustedPrice, int quantityBusted, int quantityLeft, java.util.Date transactTime, java.lang.String symbol, FillOptions options) {
        System.out.printf("A US-Equities order is busted eventId=%d, orderId=%d, reversedSide=%d, bustedPrice=%d, quantityBusted=%d, quantityLil.Date transactTime, symbol=%s,options=%s\n", eventId, orderId, reversedSide, bustedPrice, quantityBusted, quantityLeft, transactTime, symbol, options.toString());
    }

    // Called upon acknowledgement by the market of a successful cancel.
    public void	onCancelAck(long eventId, long orderId) {
        System.out.printf("An order has been canceled eventId=%d, orderId=%d\n", eventId, orderId);
    }

    // Called upon rejection of a cancel attempt by either the trading-server or the market.
    public void	onCancelReject(long eventId, long orderId, java.lang.String reason) {
        System.out.printf("A cancel has been rejected eventid=%d, orderId=%d, reason=%s\n", eventId, orderId, reason);
    }

    // Called when the client successfully connects to the trading-server.
    public void	onConnect() {
        System.out.println("CONNECTED!!!");
    }

    // Called when a fill correction occurs on an US-Equities order.
    public void	onCorrect(long eventId, long orderId, Side side, long newFillPrice, int newFilledQuantity, int quantityLeft, java.util.Date transactTime, java.lang.String symbol, FillOptions options) {
        System.out.printf("A US-Equities order fill has been corrected eventId=%d, orderId=%d, side=%s, newFillPrice=%d, newFilledQuantity=%d, quantityLeft=%d, transactTime=%s, symbol=%s, options=%s\n", eventId, orderId, side, newFillPrice, newFilledQuantity, quantityLeft, transactTime, symbol, options);
    }

    // Called if an existing connection to the trading-server is broken (whether intentionally or unexpectedly), or if a connection attempt to the trading-server fails.
    public void	onDisconnect(java.lang.String reason) {
        System.out.printf("Server disconnected reason=%s\n", reason);
    }

    // Called when a fill occurs on an US-Equities order.
    public void	onFill(long eventId, long orderId, Side side, int liquidity, long fillPrice, int quantityFilled, int quantityLeft, java.util.Date transactTime, java.lang.String symbol, FillOptions options) {
        System.out.printf("A US-Equities order is filled eventId=%d, orderId=%d, side=%d, liquidity=%d, fillPrice=%d, quantityFilled=%d, quantityLeft=%d, transactTime=%s, symbol=%s, options=%s\n", eventId, orderId, side, liquidity, fillPrice, quantityFilled, quantityLeft, transactTime, symbol, options.toString());
    }

    // Called upon acknowledgment by the market of a successful new order (not a cancel-replace).
    public void	onOrderAck(long eventId, long orderId, long limeOrderId, AckOptions options) {
        System.out.printf("An order as acknowledged eventId=%d, orderId=%d, limeOrderId=%d, options=%s\n", eventId, orderId, limeOrderId, options.toString());
    }

    // Called when a new US-Equities order is placed via some alternative mechanism into the trading-server (for example, via Portal).
    public void	onOrderEcho(long eventId, long orderId, Side side, long price, int quantity, java.lang.String route, java.lang.String symbol) {
        System.out.printf("A US-Equities order was sent using an alternative method eventId=%d, orderId=%d, side=%s, price=%d, quantity=%d, route=%s, symbol=%s\n", eventId, orderId, side, price, quantity, route, symbol);
    }

    // Called upon rejection of a new order (not cancel-replace) by either the trading-server or the market.
    public void	onOrderReject(long eventId, long orderId, java.lang.String reason) {
        System.out.printf("A new order was rejected eventId=%d, orderId=%d, reason=%s\n", eventId, orderId, reason);
    }

    // Called upon acknowledgement by the market of a successful partial cancel.
    public void	onPartialCancelAck(long eventId, long orderId, int quantityLeft) {
        System.out.printf("The market has partially canceled an order eventId=%d, orderId=%d, quantityLeft=%d\n", eventId, orderId, quantityLeft);
    }

    // Called upon acknowledgement by the market of a successful cancel-replace.
    public void	onReplaceAck(long eventId, long origOrderId, long orderId, long limeOrderId, AckOptions options) {
        System.out.printf("An order was canceld and replaced  eventId=%d, origOrderId=%d, orderId=%d, limeOrderId=%d, options=%s\n", eventId, origOrderId, orderId, limeOrderId, options);
    }

    // Called when a cancel-replace is attempted via some alternative mechanism (for example, via Portal).
    public void	onReplaceEcho(long eventId, long origOrderId, long orderId, long price, int quantity) {
        System.out.printf("A cancel replace operation was attempted using an alternative mechanism eventId=%d, origOrderId=%d, orderId=%d, price=%d, quantity=%d\n", eventId, origOrderId, orderId, price, quantity);
    }

    // Called upon rejection of a cancel-replace by either the trading-server or the market.
    public void	onReplaceReject(long eventId, long origOrderId, long orderId, java.lang.String reason) {
        System.out.printf("A cancel replace operation was rejected eventId=%d, origOrderId=%d, orderId=%d, reason=%s\n", eventId, origOrderId, orderId, reason);
    }

    // Called when a previous fill on an US-Options order is busted (reversed).
    public void	onUSOptionsBust(long eventId, long orderId, Side reversedSide, long bustedPrice, int quantityBusted, int quantityLeft, java.util.Date transactTime, USOptionsSymbol symbol, FillOptions options) {
        System.out.printf("A previously filled US Option is busted eventId=%d, orderId=%d, reversedSide=%s, bustedPrice=%d, quantityBusted=%d, quantityLeft=%d, transactTime=%s, symbol=%s, options=%s\n", eventId, orderId, reversedSide, bustedPrice, quantityBusted, quantityLeft, transactTime, symbol, options);
    }

    // Called when a fill correction occurs on an US-Options order.
    public void	onUSOptionsCorrect(long eventId, long orderId, Side side, long newFillPrice, int newFilledQuantity, int quantityLeft, java.util.Date transactTime, USOptionsSymbol symbol, FillOptions options) {
        System.out.printf("A US Option order's fill is corrected\n", eventId, orderId, side, newFillPrice, newFilledQuantity, quantityLeft,  transactTime,  symbol, options);
    }

    // Called when a fill occurs on an US-Options order.
    public void	onUSOptionsFill(long eventId, long orderId, Side side, int liquidity, long fillPrice, int quantityFilled, int quantityLeft, java.util.Date transactTime, USOptionsSymbol symbol, FillOptions options) {
        System.out.printf("A US Option is filled eventId=%d, orderId=%d, side=%s, liquidity=%d, fillPrice=%d, quantityFilled=%d, quantityLeft=%d, transactTime=%s, symbol=%s, options=%s\n", eventId, orderId, side, liquidity, fillPrice, quantityFilled, quantityLeft, transactTime, symbol, options);
    }

    // Called when a new US-Options order is placed via some alternative mechanism into the trading-server (for example, via Portal).
    public void	onUSOptionsOrderEcho(long eventId, long orderId, Side side, long price, int quantity, PositionEffect positionEffect, java.lang.String route, USOptionsSymbol symbol) {
        System.out.printf("A US Option was placed using an alternative mechanism eventId=%d, orderId=%d, side=%s, price=%d, quantity=%d, positionEffect=%s, route=%s, symbol=%s\n", eventId, orderId, side, price, quantity, positionEffect, route, symbol);
    }
}
