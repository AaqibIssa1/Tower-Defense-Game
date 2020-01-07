package edu.coms.sr2.game.http;

public enum HttpMethod {
    GET { @Override public String toString() { return "GET"; } },
    POST{ @Override public String toString() { return "POST"; } }
}