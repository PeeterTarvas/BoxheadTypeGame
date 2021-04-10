package com.mygdx.game.Server;

import com.mygdx.game.Characters.Zombie;
import com.mygdx.game.Server.ServerConnection;

import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.sleep;

public class ServerUpdateThread implements Runnable {

    private ServerConnection serverConnection;
    private int countBetweenSendingNewZombies = 0;
    private int newZombiesSent = 0;

    public void setServerConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    public ServerConnection getServerConnection() {
        return serverConnection;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (serverConnection.serverWorld.hasClients()) {
                    // Siin on mängu andmete uuendamise meetodid.
                    serverConnection.updateBullets();

                    // Saadab zombied, kes tuleb mängust eemaldada.
                    // Selle saaks äkki kuidagi sendUpdatedZombies meetodiga ühendada?
                    if (!serverConnection.serverWorld.getZombiesToBeRemoved().isEmpty()) {
                        serverConnection.sendRemoveZombiesFromGame(serverConnection.serverWorld.getAndEmptyZombiesToBeRemovedList());
                    }

                    // Wave
                    if (serverConnection.serverWorld.isNewWave() && newZombiesSent < serverConnection.serverWorld.getZombiesInWave() && countBetweenSendingNewZombies == 250) {
                        List<Zombie> newZombies = serverConnection.serverWorld.createZombies();
                        newZombiesSent += 4;
                        countBetweenSendingNewZombies = 0;
                        serverConnection.sendNewZombies(newZombies);
                    } else if (newZombiesSent == serverConnection.serverWorld.getZombiesInWave()) {
                        serverConnection.serverWorld.setNewWave(false);
                        newZombiesSent = 0;
                        countBetweenSendingNewZombies = 0;
                    } else if (serverConnection.serverWorld.isNewWave() && newZombiesSent < serverConnection.serverWorld.getZombiesInWave()) {
                        countBetweenSendingNewZombies += 1;
                    }

                    // Saadab uuendatud zombied.
                    if (!serverConnection.serverWorld.getZombieMap().isEmpty()) {  // Siin on viga -> saadetakse, tühi map, aga see vist ei sega hetkel mängu.
                        // System.out.println("serverConnection.sendUpdatedZombies");
                        // System.out.println(serverConnection.serverWorld.getZombieMap());
                        serverConnection.sendUpdatedZombies();
                    }
                }
                sleep(5);
            } catch (Exception e) {
                //System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }
    }
}
