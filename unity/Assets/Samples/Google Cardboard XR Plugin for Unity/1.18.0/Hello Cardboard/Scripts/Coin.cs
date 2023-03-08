using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Schema;
using UnityEngine;
using UnityEngine.Experimental.GlobalIllumination;
using UnityEngine.UI;

class Coin : MonoBehaviour
{
    public GameObject[] coins;
    public GameObject coin;
    public GameObject textObject;
    private Text textShow;
    public float threhold=4f;
    private int numberOfCoin = 3;
    private int gotNumber = 0;
    private int partnerNumber = 0;

    private void Start()
    {
        coins = new GameObject[numberOfCoin];
        textShow=textObject.GetComponent<Text>();
       
        textShow.text = "您已经获得的金币数目：0\n您的对手获得的金币数目:0 ";
        for (int i = 0; i < numberOfCoin; i++)
        {
            GameObject prefabInstance = Instantiate(coin, new Vector3(2.5f + i * 5.0f, 1f, 2.5f + i * 5), Quaternion.identity);
            //prefabInstance.transform.parent = coinContainer.transform;
            coins[i] = prefabInstance;
            prefabInstance.transform.Rotate(new Vector3(90f, 90f, 90f));
           
        }

    }
    //x,y,是目标用户的
    private bool judgeHide(GameObject coin,float x, float z,bool isUp)
    {   
        float myx=coin.transform.position.x;
        float myz = coin.transform.position.x;
        float square=(x-myx)* (x - myx) + (z- myz) * (z- myz);
        return square - threhold < 0 && isUp&&coin.GetComponent<Renderer>().enabled;
    }

    void Update()
    {


        foreach (GameObject e in coins)
        {
            
            if (judgeHide(e, userDate.xx, userDate.zz, userDate.mainUp))
            {
                e.GetComponent<Renderer>().enabled = false;
                gotNumber++;
                textShow.text = "您已经获得的金币数目" + gotNumber + "\n您的对手获得的金币数目: " + partnerNumber;
               
            }
            else if (judgeHide(e, userDate.x2, userDate.z2, userDate.partnerUP))
            {
                e.GetComponent<Renderer>().enabled = false;
                partnerNumber++;
                textShow.text = "您已经获得的金币数目"+gotNumber+"\n您的对手获得的金币数目: "+partnerNumber;

            }
        }
    }
}