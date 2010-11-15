package br.uff.ic.gardener.patch.deltaitem;

import br.uff.ic.gardener.patch.chunk.Chunk;

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public class NormalDeltaItem extends BasicDeltaItem implements DeltaItem {

    /** Field description */
    private String operation;

    /**
     *
     * @param info1
     * @param info2
     * @param chunks
     * @param operation
     */
    public NormalDeltaItem( DeltaItemInfo info1, DeltaItemInfo info2, LinkedList<Chunk> chunks, String operation ) {
        super( info1, info2, chunks );
        setOperation( operation );
    }

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();

        text.append( summaryToString( this.getOriginalFileInfo() ) );
        text.append( getOperation() );
        text.append( summaryToString( this.getNewFileInfo() ) );
        text.append( "\n" );

        return text.toString();
    }

    /**
     *
     * @param info
     * @return
     */
    protected String summaryToString( DeltaItemInfo info ) {
        StringBuilder text = new StringBuilder();

        if (info.getLenght() == 0) {
            text.append( info.getStart() );
        } else {
            text.append( info.getStart() );
            text.append( "," );
            text.append( info.getLenght() );
        }

        return text.toString();
    }

    /**
     * @return the operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * @param operation the operation to set
     */
    public void setOperation( String operation ) {
        this.operation = operation;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean isAddOperation() {
        return getOperation().equals( "a" );
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean isDelOperation() {
        return getOperation().equals( "d" );
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean isChangeOperation() {
        return getOperation().equals( "c" );
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
