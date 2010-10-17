
//
//
//Generated by StarUML(tm) Java Add-In
//
//@ Project : Gardener
//@ File Name : Delta.java
//@ Date : 16/10/2010
//@ Author :
//
//
package br.uff.ic.gardener.patch.delta;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.chunk.Chunk;

//~--- JDK imports ------------------------------------------------------------

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public class Delta {    
    private Info              info1;
    private Info              info2;
    private LinkedList<Chunk> chunks;

    /**
     * @return the info1
     */
    public Info getInfo1() {
        return info1;
    }

    /**
     * @param info1 the info1 to set
     */
    public void setInfo1(Info info1) {
        this.info1 = info1;
    }

    /**
     * @return the info2
     */
    public Info getInfo2() {
        return info2;
    }

    /**
     * @param info2 the info2 to set
     */
    public void setInfo2(Info info2) {
        this.info2 = info2;
    }

    /**
     * @return the deltas
     */
    public LinkedList<Chunk> getChunks() {
        return chunks;
    }

    /**
     * @param chunks
     */
    public void setChunks(LinkedList<Chunk> chunks) {
        for (Chunk aChunk : chunks) {
            this.chunks.add((Chunk) aChunk.clone());
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
