using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Unity.VisualScripting;
using UnityEngine;

using System.Collections;
class User:MonoBehaviour
{
    public GameObject mainPlayer;
    public GameObject player2;
   
    void Start()
    {
        
    }
    void Update()
    {

        mainPlayer.transform.position=new Vector3(userDate.xx,0,userDate.zz);
        player2.transform.position = new Vector3(userDate.x2,0, userDate.z2);
    }
}
