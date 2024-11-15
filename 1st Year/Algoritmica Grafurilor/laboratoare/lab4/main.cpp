#include <iostream>
#include "p1.h"
#include "p2.h"
#include "p3.h"
#include "p4.h"
#include "p5.h"

using namespace std;

int main() {
    int optiune;

    while (true){
        printf("Introduceti optiunea: \n");
        cin>>optiune;
        if(optiune==1){
            p1();
        }
        else if(optiune==2){
            p2();
        }
        else if(optiune==3){
            p3();
        }
        else if(optiune==4){
            p4();
        }
        else if(optiune==5){
            p5();
        }
        else{
            break;
        }
    }
}
