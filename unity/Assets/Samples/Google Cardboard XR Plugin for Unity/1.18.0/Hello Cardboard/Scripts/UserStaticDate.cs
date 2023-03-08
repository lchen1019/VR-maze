using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
class userDate
{
    public static float xx;
    public static float yy;
    public static float zz;
    public static float x2;
    public static float y2;
    public static float z2;
    public static bool mainUp ;
    public static bool partnerUP;
    public static void setMainplayer(int x, int z)
    {
        xx = x;
        zz= z;
    }
    public static void setPlayer2(int x, int z)
    {
        x2 = x;
        z2 = z;
    }
}
