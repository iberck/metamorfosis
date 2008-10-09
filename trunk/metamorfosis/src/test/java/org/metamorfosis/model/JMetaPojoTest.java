/**
 *  Copyright (c) 2007-2008 by Carlos Gómez Montiel <iberck@gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  his program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.metamorfosis.model;

import org.metamorfosis.model.JMetaPojo;
import junit.framework.TestCase;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author iberck
 */
public class JMetaPojoTest extends TestCase {

    private static final Log log = LogFactory.getLog(JMetaPojoTest.class);

    public JMetaPojoTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Simula como lo haria el template engine
     */
    public void testJMetaPojo() throws Exception {
        MyBean b = new MyBean();
        b.setNombre("iberck");

//        JMetaPojo metaClass = new JMetaPojo("org.metamorfosis.model.MyBean");
        JMetaPojo metaClass = new JMetaPojo(b);
        metaClass.injectPojoProperty("edad", 5);

        // obtener un objeto inyectado
        Object injectedObject = metaClass.createInjectedObject();

        // test de la inyeccion del objeto
        Object clazz = PropertyUtils.getNestedProperty(injectedObject, "class.simpleName");
        assertEquals("MyBean", clazz);

        Object[] declFields = (Object[]) PropertyUtils.getNestedProperty(injectedObject, "class.declaredFields");
        assertEquals("Object[]", declFields.getClass().getSimpleName());

        // test de las propiedades originales del objeto
        assertEquals("iberck", PropertyUtils.getNestedProperty(injectedObject, "nombre"));
        assertEquals("default_value", PropertyUtils.getNestedProperty(injectedObject, "cadenaConValor"));
        assertEquals("5", PropertyUtils.getNestedProperty(injectedObject, "edad").toString());

        // test con el nombre de la clase
        JMetaPojo metaClassName = new JMetaPojo("org.metamorfosis.model.MyBean");

        metaClassName.injectPojoProperty("estatura", 23.4f);

        Object injObj = metaClassName.createInjectedObject();

        // es de tipo object ya que no se esta tomando de una instancia si no de la clase y como no tiene valor por default, es object
        assertEquals("java.lang.Object", PropertyUtils.getNestedProperty(injObj, "nombre").getClass().getName());
        assertNotNull(PropertyUtils.getNestedProperty(injObj, "nombre"));

        assertEquals("java.lang.String", PropertyUtils.getNestedProperty(injObj, "cadenaConValor").getClass().getName());
        assertEquals("default_value", PropertyUtils.getNestedProperty(injObj, "cadenaConValor"));

        assertEquals("java.lang.Float", PropertyUtils.getNestedProperty(injObj, "estatura").getClass().getName());
        assertEquals(23.4f, PropertyUtils.getNestedProperty(injObj, "estatura"));

        // reinjectar y probar
        metaClassName.injectPojoProperty("otraPropiedad", "val");
        injObj = metaClassName.createInjectedObject();

        // valores viejos
        assertEquals("java.lang.Object", PropertyUtils.getNestedProperty(injObj, "nombre").getClass().getName());
        assertNotNull(PropertyUtils.getNestedProperty(injObj, "nombre"));
        assertEquals("java.lang.String", PropertyUtils.getNestedProperty(injObj, "cadenaConValor").getClass().getName());
        assertEquals("default_value", PropertyUtils.getNestedProperty(injObj, "cadenaConValor"));
        assertEquals("java.lang.Float", PropertyUtils.getNestedProperty(injObj, "estatura").getClass().getName());
        assertEquals(23.4f, PropertyUtils.getNestedProperty(injObj, "estatura"));

        // nuevo valor inyectado
        assertEquals("java.lang.String", PropertyUtils.getNestedProperty(injObj, "otraPropiedad").getClass().getName());
        assertEquals("val", PropertyUtils.getNestedProperty(injObj, "otraPropiedad"));
    }

    public void testJMetaPojoInjectedField() throws Exception {
        JMetaPojo metaClass = new JMetaPojo("org.metamorfosis.model.MyBean");

        metaClass.injectFieldProperty("nombre", "pref", "nom");
        Object injObj = metaClass.createInjectedObject();

        Object nombreNP = PropertyUtils.getNestedProperty(injObj, "nombre");
        // metapojo por que es un metafield
        assertEquals("JMetaPojo", nombreNP.getClass().getSimpleName());
        Object prefNp = PropertyUtils.getNestedProperty(nombreNP, "pref");
        assertEquals("nom", prefNp);

        Object nombrePreNP = PropertyUtils.getNestedProperty(injObj, "nombre.pref");
        assertEquals("java.lang.String", nombrePreNP.getClass().getName());
        assertEquals("nom", nombrePreNP);


        Object[] decFields = (Object[]) PropertyUtils.getNestedProperty(injObj, "class.declaredFields");
        assertNotNull(decFields);
    }
}
