//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ik.analyzer.lucene;

import java.io.IOException;
import java.io.Reader;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.Version;
import com.ik.analyzer.core.IKSegmenter;
import com.ik.analyzer.core.Lexeme;

public final class IKTokenizer extends Tokenizer {
    private IKSegmenter _IKImplement;
    private final CharTermAttribute termAtt;
    private final OffsetAttribute offsetAtt;
    private final TypeAttribute typeAtt;
    private int endPosition;
    private Version version;

    public IKTokenizer(Reader in, boolean useSmart) {
        this.version = Version.LATEST;
        this.offsetAtt = (OffsetAttribute)this.addAttribute(OffsetAttribute.class);
        this.termAtt = (CharTermAttribute)this.addAttribute(CharTermAttribute.class);
        this.typeAtt = (TypeAttribute)this.addAttribute(TypeAttribute.class);
        this._IKImplement = new IKSegmenter(this.input, useSmart);
    }

    public boolean incrementToken() throws IOException {
        this.clearAttributes();
        Lexeme nextLexeme = this._IKImplement.next();
        if(nextLexeme != null) {
            this.termAtt.append(nextLexeme.getLexemeText());
            this.termAtt.setLength(nextLexeme.getLength());
            this.offsetAtt.setOffset(nextLexeme.getBeginPosition(), nextLexeme.getEndPosition());
            this.endPosition = nextLexeme.getEndPosition();
            this.typeAtt.setType(nextLexeme.getLexemeTypeString());
            return true;
        } else {
            return false;
        }
    }

    public void reset() throws IOException {
        super.reset();
        this._IKImplement.reset(this.input);
    }

    public final void end() {
        int finalOffset = this.correctOffset(this.endPosition);
        this.offsetAtt.setOffset(finalOffset, finalOffset);
    }
}
