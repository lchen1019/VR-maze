using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Text;
using System.Threading.Tasks;
using Unity.VisualScripting;
using UnityEngine;

public class Wall : MonoBehaviour
{
    public GameObject wall;
    public GameObject wallContainer;
    private GameObject[] walls;
    //maze size,  numOfGrid X  numOfGrid girds  
    private const int numOfGrid = 4;
    //compute edge if exist
    //total  2*numOfGrid*
    int wallLength = 5;
    int wallThickness = 1;
    private static bool[] wallStatus= new bool[2 * (numOfGrid + 1) * numOfGrid];
  
    void Start()
    {           
            initWallStatus();
            generateWalls();

    }
    void generateWalls()
    {
        for (int i = 0; i < numOfGrid+1; i++)
            for (int j = 0; j < numOfGrid; j++)
            {
                if (getWallExisted(0, i, j))
                {
                    GameObject prefabInstance = Instantiate(wall, new Vector3((i-1)* wallLength, 0, j* wallLength-(wallLength-wallThickness)/2.0f), Quaternion.identity);
                    prefabInstance.transform.parent = wallContainer.transform;
                    walls[map(0,i,j)]= prefabInstance;
                }
                if (getWallExisted(1, i, j))
                {
                    GameObject prefabInstance = Instantiate(wall, new Vector3(j * wallLength-(wallLength-wallThickness)/2.0f , 0, (i-1) * wallLength  ), Quaternion.Euler(0,90,0));
                    prefabInstance.transform.parent = wallContainer.transform;
                    walls[map(1, i, j)] = prefabInstance;
                }
            }
    }
    private void initWallStatus()
    {
      //  wallStatus 
        walls = new GameObject[2 * (numOfGrid + 1) * numOfGrid];
        //--test--
        for(int i = 0; i < wallStatus.Length; ++i)
        {
            wallStatus[i] = true;
        }
        //wallStatus[5] = false;
        //wallStatus[7] = false;
        //--test--
    }
    //we assume map is 
    //  dimension experss is horizontal or vertical  0表示行（横着的边） 1表示列
    //  layer是第几层，取值 0- numOfGrid-1  offset表示该行第几个
    private int map(int dimension, int layer, int offset)
    {
        return (numOfGrid + 1) * numOfGrid * dimension + layer * numOfGrid + offset;
    }
    private  bool getWallExisted(int  dimension,int layer,int offset)
    { 
        return wallStatus[map(dimension,layer,offset)];
    }
    public static void setWallsStatuis(char[] chars)
    {
        Debug.Log(chars);
        for (int i=0;i<wallStatus.Length; ++i)
        {
            if (chars[i]=='0')
                wallStatus[i] = false;
            else
                wallStatus[i] = true;
        }
        
    }
    private void updateWalls()
    {
        for (int i = 0; i < numOfGrid + 1; i++)
            for (int j = 0; j < numOfGrid; j++)
            {
                if (walls[map(0, i, j)] == null)
                { Debug.Log("0 "+i + "  " + j); 
                }
                walls[map(0, i, j)].GetComponent<Renderer>().enabled = wallStatus[map(0, i, j)];
                walls[map(1, i, j)].GetComponent<Renderer>().enabled = wallStatus[map(1, i, j)];
            }
        
    }
    
    void Update()
    {
        
        updateWalls();
    }
}