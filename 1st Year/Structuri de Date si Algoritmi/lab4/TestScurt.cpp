#include "TestScurt.h"
#include "Colectie.h"
#include "IteratorColectie.h"
#include <assert.h>
#include <iostream>

using namespace std;

void testAll() {
	Colectie c;
	c.adauga(5);
	c.adauga(6);
	c.adauga(0);
	c.adauga(5);
	c.adauga(10);
	c.adauga(8);

	assert(c.dim() == 6);
	assert(c.nrAparitii(5) == 2);

	assert(c.sterge(5) == true);
	assert(c.dim() == 5);

	assert(c.cauta(6) == true);
	assert(c.vida() == false);

	IteratorColectie ic = c.iterator();
	assert(ic.valid() == true);
	while (ic.valid()) {
		ic.element();
		ic.urmator();
	}
	assert(ic.valid() == false);
	ic.prim();
	assert(ic.valid() == true);

	// testare transformaInMultime
	Colectie c2;
	assert(c2.dim() == 0);

	for (int i = 0; i < 5; i++) {
		for (int j = 0; j < 2; j++)
			c2.adauga(i);
	}
	assert(c2.dim() == 10);

	int k = c2.transformaInMultime();
	assert(c2.dim() == 5);
	assert(k == 5);

	k = c2.transformaInMultime();
	assert(c2.dim() == 5);
	assert(k == 0);

}