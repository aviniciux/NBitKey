

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SHA256G {

    long[] Kzh = {
        0x428A2F98L, 0x71374491L, 0xB5C0FBCFL, 0xE9B5DBA5L,
        0x3956C25BL, 0x59F111F1L, 0x923F82A4L, 0xAB1C5ED5L,
        0xD807AA98L, 0x12835B01L, 0x243185BEL, 0x550C7DC3L,
        0x72BE5D74L, 0x80DEB1FEL, 0x9BDC06A7L, 0xC19BF174L,
        0xE49B69C1L, 0xEFBE4786L, 0x0FC19DC6L, 0x240CA1CCL,
        0x2DE92C6FL, 0x4A7484AAL, 0x5CB0A9DCL, 0x76F988DAL,
        0x983E5152L, 0xA831C66DL, 0xB00327C8L, 0xBF597FC7L,
        0xC6E00BF3L, 0xD5A79147L, 0x06CA6351L, 0x14292967L,
        0x27B70A85L, 0x2E1B2138L, 0x4D2C6DFCL, 0x53380D13L,
        0x650A7354L, 0x766A0ABBL, 0x81C2C92EL, 0x92722C85L,
        0xA2BFE8A1L, 0xA81A664BL, 0xC24B8B70L, 0xC76C51A3L,
        0xD192E819L, 0xD6990624L, 0xF40E3585L, 0x106AA070L,
        0x19A4C116L, 0x1E376C08L, 0x2748774CL, 0x34B0BCB5L,
        0x391C0CB3L, 0x4ED8AA4AL, 0x5B9CCA4FL, 0x682E6FF3L,
        0x748F82EEL, 0x78A5636FL, 0x84C87814L, 0x8CC70208L,
        0x90BEFFFAL, 0xA4506CEBL, 0xBEF9A3F7L, 0xC67178F2L

    };

    long[] H0 = {
        0x6A09E667L, 0xBB67AE85L, 0x3C6Ef372L, 0xA54FF53AL,
        0x510E527FL, 0x9B05688CL, 0x1F83D9ABL, 0x5BE0CD19L
    };

    public void sha256_core(long[] W, long[] Hi0, long[] Hi) {
        long A, B, C, D, E, F, G, H, temp1, temp2;
        int t;

        // Calculating W t values after round 16
        for (t = 16; t < 64; t++) {
            W[t] = setRange(S1(W[t - 2]) + W[t - 7] + S0(W[t - 15]) + W[t - 16]);
        }
        ///////////////////////////////////////////////////////////
        // Message compression
        // Para t = 0
        // Faz a primeira iteracao antes do loop 
        // para evitar inicializacao de A, B, C, D, E, F, G, e H	
        ///////////////////////////////////////////////////////////        
        temp1 = setRange(Hi0[7] + S3(Hi0[4]) + CH(Hi0[4], Hi0[5], Hi0[6]) + Kzh[0] + W[0]);
        temp2 = setRange(S2(Hi0[0]) + MAJ(Hi0[0], Hi0[1], Hi0[2]));
        H = Hi0[6];
        G = Hi0[5];
        F = Hi0[4];
        E = setRange(Hi0[3] + temp1);
        D = Hi0[2];
        C = Hi0[1];
        B = Hi0[0];
        A = setRange(temp1 + temp2);

        // Message compression
        // Para t = 1 a t = 62
        for (t = 1; t < 63; t++) {
            temp1 = setRange(H + S3(E) + CH(E, F, G) + Kzh[t] + W[t]);
            temp2 = setRange(S2(A) + MAJ(A, B, C));
            H = G;
            G = F;
            F = E;
            E = setRange(D + temp1);
            D = C;
            C = B;
            B = A;
            A = setRange(temp1 + temp2);
        }
        ///////////////////////////////////////////////////////////
        // Para t = 63
        // Faz a ultima iteracao depois do loop 
        // para evitar uma operacao extra
        ///////////////////////////////////////////////////////////
        temp1 = setRange(H + S3(E) + CH(E, F, G) + Kzh[63] + W[63]);
        temp2 = setRange(S2(A) + MAJ(A, B, C));
        Hi[0] = setRange((temp1 + temp2) + Hi0[0]);
        Hi[1] = setRange(A + Hi0[1]);
        Hi[2] = setRange(B + Hi0[2]);
        Hi[3] = setRange(C + Hi0[3]);
        Hi[4] = setRange((D + temp1) + Hi0[4]);
        Hi[5] = setRange(E + Hi0[5]);
        Hi[6] = setRange(F + Hi0[6]);
        Hi[7] = setRange(G + Hi0[7]);

    }

    // Shift right
    public long setRange(long val) {
        return (val & 0xFFFFFFFFL);

    }

    // Shift right
    public long SHR(long x, long n) {
        //long tmp = ((x & 0xFFFFFFFF) >> n);
        return ((x & 0xFFFFFFFFL) >> n);

    }

    // Rotate right
    public long ROTR(long x, int n) {
        long tmp1 = setRange(x << (32 - n));
        return (SHR(x, n) | tmp1);

    }

    // S0 and S1
    public long S0(long x) {
        return (ROTR(x, 7) ^ ROTR(x, 18) ^ SHR(x, 3));

    }

    public long S1(long x) {
        return (ROTR(x, 17) ^ ROTR(x, 19) ^ SHR(x, 10));

    }

    // Σ0 and Σ1
    public long S2(long x) {
        return (ROTR(x, 2) ^ ROTR(x, 13) ^ ROTR(x, 22));

    }

    public long S3(long x) {
        return (ROTR(x, 6) ^ ROTR(x, 11) ^ ROTR(x, 25));

    }

    // Maj and Ch
    public long MAJ(long x, long y, long z) {
        return ((x & y) | (z & (x | y)));

    }

    public long CH(long x, long y, long z) {
        return (z ^ (x & (y ^ z)));

    }

   
    public long StringToHex(String texto) {
        long x = Long.parseLong(texto, 16);
        return x;

    }

  
    public String toHexString(BigInteger input) {
        return String.format("%x", input);
        
    }
     
    public String fromHexString(String hex) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            str.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
        }
        return str.toString();

    }

    public String AppendZero(String auxiliar) {
        StringBuilder construtor = new StringBuilder();
        construtor.append(auxiliar);
        while (construtor.length() % 8 != 0) {
            construtor.append("0");
        }
        return construtor.toString();
    }

    public BigInteger getSHA(BigInteger input){
        
        SHA256G core = new SHA256G();
        
        long[] W = {
            0x00000000, 0x00000000, 0x00000000, 0x00000000,
            0x00000000, 0x00000000, 0x00000000, 0x00000000,
            0x00000000, 0x00000000, 0x00000000, 0x00000000,
            0x00000000, 0x00000000, 0x00000000, 0x00000000,
            0x00000000, 0x00000000, 0x00000000, 0x00000000,
            0x00000000, 0x00000000, 0x00000000, 0x00000000,
            0x00000000, 0x00000000, 0x00000000, 0x00000000,
            0x00000000, 0x00000000, 0x00000000, 0x00000000,
            0x00000000, 0x00000000, 0x00000000, 0x00000000,
            0x00000000, 0x00000000, 0x00000000, 0x00000000,
            0x00000000, 0x00000000, 0x00000000, 0x00000000,
            0x00000000, 0x00000000, 0x00000000, 0x00000000,
            0x00000000, 0x00000000, 0x00000000, 0x00000000,
            0x00000000, 0x00000000, 0x00000000, 0x00000000,
            0x00000000, 0x00000000, 0x00000000, 0x00000000,
            0x00000000, 0x00000000, 0x00000000, 0x00000000

        };
        long[] Hi = {
            0x00000000, 0x00000000, 0x00000000, 0x00000000,
            0x00000000, 0x00000000, 0x00000000, 0x00000000
        };

        String mensagem = toHexString(input) + "8"; 
        mensagem = AppendZero(mensagem);
        
        String texto_auxiliar;
        String quartenario;
        int tamanho_bits;
        boolean first = true;
        int x = 0;
        int y = 0;
        int z = 0;
        
        while(x <= mensagem.length()) {
            
            if(x+128 > mensagem.length()){
                texto_auxiliar = mensagem.substring(x);
            }
            else{
                texto_auxiliar = mensagem.substring(x, x+128);
            }       
           
            while(y < texto_auxiliar.length()){
                quartenario = texto_auxiliar.substring(y, y+8);
                W[z] = StringToHex(quartenario);
                y = y + 8;
                z = z + 1;
            }
            
            if(texto_auxiliar.length() < 114){
                tamanho_bits = toHexString(input).length()*4;
                quartenario = String.format("%08x", tamanho_bits);   
                quartenario = AppendZero(quartenario);            
                W[15] = StringToHex(quartenario);
                y++;
            }
            if(first){
                core.sha256_core(W, core.H0, Hi);
                first = false;
                
            }
            else{
                core.sha256_core(W, Hi, Hi);
                
            }
            
            W[0] = 0x00000000; W[1] = 0x00000000; W[2] = 0x00000000; W[3] = 0x00000000;
            W[4] = 0x00000000; W[5] = 0x00000000; W[6] = 0x00000000; W[7] = 0x00000000;
            W[8] = 0x00000000; W[9] = 0x00000000; W[10] = 0x00000000; W[11] = 0x00000000;
            W[12] = 0x00000000; W[13] = 0x00000000; W[14] = 0x00000000; W[15] = 0x00000000;
            
            x += y; y = 0; z = 0;
              
        }        
        
        //System.out.printf("%x%x%x%x%x%x%x%x \n", Hi[0], Hi[1], Hi[2], Hi[3], Hi[4], Hi[5], Hi[6], Hi[7]);
        //StringBuilder auxiliar = new StringBuilder();
        //for(int i = 0; i < 8; i++){
        //    auxiliar.append(String.format("%x", Hi[i]));
        //}
        BigInteger n = BigInteger.valueOf(Hi[0]);
        BigInteger m = new BigInteger("4294967296");
        for(int i = 1; i < 8; i++){
               n = n.multiply(m).add(BigInteger.valueOf(Hi[i]));
        }
        
        return n;
        
    }
    
}
