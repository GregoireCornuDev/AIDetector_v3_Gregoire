int main() {						  					// priorite = 1 

	affiche("demarrage de main");		
									// pour etape 3
	sem1 = alloueSemaphore(1);
	sem2 = alloueSemaphore(0);
	blocage = alloueSemaphore(0);						// p1 est le premier a se terminer 

	int pid1 = creeProcessus(PLUS_FORTE_PRIORITE,p1); 	// p1 demarre aussitôt
	int pid2 = creeProcessus(PLUS_FORTE_PRIORITE,p2);

			 
	P(blocage);							

	detruitProcessus(pid1);				
	detruitProcessus(pid2);				
	detruitProcessus(obtientPid());		

	affiche("fin de main");				
		  
	return 0; // pour compilateur
}
