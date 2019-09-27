/**
 * PacketWrapper - ProtocolLib wrappers for Minecraft packets
 * Copyright (C) dmulloy2 <http://dmulloy2.net>
 * Copyright (C) Kristian S. Strangeland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net._4kills.particles.packet;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.base.Objects;

public abstract class AbstractPacket {
    // The particleeffects.packet we will be modifying
    protected PacketContainer handle;

    /**
     * Constructs a new strongly typed wrapper for the given particleeffects.packet.
     *
     * @param handle - handle to the raw particleeffects.packet data.
     * @param type - the particleeffects.packet type.
     */
    protected AbstractPacket(PacketContainer handle, PacketType type) {
        // Make sure we're given a valid particleeffects.packet
        if (handle == null)
            throw new IllegalArgumentException("Packet handle cannot be NULL.");
        if (!Objects.equal(handle.getType(), type))
            throw new IllegalArgumentException(handle.getHandle()
                    + " is not a particleeffects.packet of type " + type);

        this.handle = handle;
    }

    /**
     * Retrieve a handle to the raw particleeffects.packet data.
     *
     * @return Raw particleeffects.packet data.
     */
    public PacketContainer getHandle() {
        return handle;
    }

    /**
     * Send the current particleeffects.packet to the given receiver.
     *
     * @param receiver - the receiver.
     * @throws RuntimeException If the particleeffects.packet cannot be sent.
     */
    public void sendPacket(Player receiver) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(receiver,
                    getHandle());
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot send particleeffects.packet.", e);
        }
    }

    /**
     * Send the current particleeffects.packet to all online players.
     */
    public void broadcastPacket() {
        ProtocolLibrary.getProtocolManager().broadcastServerPacket(getHandle());
    }

    /**
     * Simulate receiving the current particleeffects.packet from the given sender.
     *
     * @param sender - the sender.
     * @throws RuntimeException If the particleeffects.packet cannot be received.
     * @deprecated Misspelled. recieve to receive
     * @see #receivePacket(Player)
     */
    @Deprecated
    public void recievePacket(Player sender) {
        try {
            ProtocolLibrary.getProtocolManager().recieveClientPacket(sender,
                    getHandle());
        } catch (Exception e) {
            throw new RuntimeException("Cannot recieve particleeffects.packet.", e);
        }
    }

    /**
     * Simulate receiving the current particleeffects.packet from the given sender.
     *
     * @param sender - the sender.
     * @throws RuntimeException if the particleeffects.packet cannot be received.
     */
    public void receivePacket(Player sender) {
        try {
            ProtocolLibrary.getProtocolManager().recieveClientPacket(sender,
                    getHandle());
        } catch (Exception e) {
            throw new RuntimeException("Cannot receive particleeffects.packet.", e);
        }
    }
}