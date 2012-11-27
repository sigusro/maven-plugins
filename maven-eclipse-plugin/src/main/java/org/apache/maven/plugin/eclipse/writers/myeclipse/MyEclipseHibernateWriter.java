package org.apache.maven.plugin.eclipse.writers.myeclipse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.eclipse.Messages;
import org.apache.maven.plugin.eclipse.writers.AbstractEclipseWriter;
import org.apache.maven.plugin.ide.IdeUtils;
import org.codehaus.plexus.util.IOUtil;

/**
 * Writer for MyEclipse Hibernate Capability
 * 
 * @author <a href="mailto:olivier.jacob@gmail.com">Olivier Jacob</a>
 */
public class MyEclipseHibernateWriter
    extends AbstractEclipseWriter
{

    private static final String MYECLIPSE_HIBERNATE_DATA_FILE = ".myhibernatedata";

    private static final String MYECLIPSE_HB_GEN_COMP_ID = "genBasicCompId";

    private static final String MYECLIPSE_HB_SESSION_FACTORY_NAME = "sessionFactoryName";

    private static final String MYECLIPSE_HB_PROFILE = "profile";

    private static final String MYECLIPSE_HB_DAO_SF_ID = "daoSFId";

    private static final String MYECLIPSE_HB_VERSION = "version";

    private static final String MYECLIPSE_HB_JNDI_PATH = "jndiPath";

    private static final String MYECLIPSE_HB_DETECT_M2M = "detectM2M";

    private static final String MYECLIPSE_HB_RE_STRATEGY_CLASS = "reStrategyClass";

    private static final String MYECLIPSE_HB_SPRING_DAO_FILE = "springDaoFile";

    private static final String MYECLIPSE_HB_USE_JAVA_TYPES = "useJavaTypes";

    private static final String MYECLIPSE_HB_KEY_GENERATOR = "keyGenerator";

    private static final String MYECLIPSE_HB_LIB_INSTALL_FOLDER = "libInstallFolder";

    private static final String MYECLIPSE_HB_ADD_LIBS_TO_PROJECT = "addLibs2Project";

    private static final String MYECLIPSE_HB_GEN_VERSION_TAG = "genVersionTag";

    private static final String MYECLIPSE_HB_SESSION_FACTORY_ID = "sessionFactoryId";

    private static final String MYECLIPSE_HB_BASE_PERSISTENT_CLASS = "basePersistenceClass";

    private static final String MYECLIPSE_HB_RE_SETTINGS_FILE = "reSettingsFile";

    private static final String MYECLIPSE_HB_CONFIG_FILE = "configFile";

    private static final String MYECLIPSE_HB_CREATE_CONFIG_FILE = "createConfigFile";

    private static final String MYECLIPSE_HB_ADD_LIBS_TO_CLASSPATH = "addLibs2Classpath";

    private static final String MYECLIPSE_HB_BASE_DAO_CLASS = "baseDaoClass";

    /**
     * Relative path to the Hibernate configuration file to use in MyEclipse
     */
    private Map hibernateConfig;

    /**
     * Constructor
     * 
     * @param hibernateConfig path to the configuration file to use
     */
    public MyEclipseHibernateWriter( Map hibernateConfig )
    {
        this.hibernateConfig = hibernateConfig;
    }

    /**
     * Write MyEclipse Hibernate configuration
     * 
     * @throws MojoExecutionException if an error occurs
     */
    public void write()
        throws MojoExecutionException
    {
        FileWriter w;

        try
        {
            w = new FileWriter( new File( config.getEclipseProjectDirectory(), MYECLIPSE_HIBERNATE_DATA_FILE ) );
        }
        catch ( IOException ex )
        {
            throw new MojoExecutionException( Messages.getString( "EclipsePlugin.erroropeningfile" ), ex ); //$NON-NLS-1$
        }

        PrintWriter pw = new PrintWriter( w );

        pw.println( "# Generated by Maven" );
        addProperty( pw, MYECLIPSE_HB_GEN_COMP_ID, "false" );
        addProperty( pw, MYECLIPSE_HB_SESSION_FACTORY_NAME, null );
        addProperty( pw, MYECLIPSE_HB_PROFILE, null );
        addProperty( pw, MYECLIPSE_HB_DAO_SF_ID, null );
        addProperty( pw, MYECLIPSE_HB_VERSION, getHibernateVersion() );
        addProperty( pw, MYECLIPSE_HB_JNDI_PATH, null );
        addProperty( pw, MYECLIPSE_HB_DETECT_M2M, "false" );
        addProperty( pw, MYECLIPSE_HB_RE_STRATEGY_CLASS, null );
        addProperty( pw, MYECLIPSE_HB_SPRING_DAO_FILE, null );
        addProperty( pw, MYECLIPSE_HB_USE_JAVA_TYPES, "true" );
        addProperty( pw, MYECLIPSE_HB_KEY_GENERATOR, null );
        addProperty( pw, MYECLIPSE_HB_LIB_INSTALL_FOLDER, null );
        addProperty( pw, MYECLIPSE_HB_ADD_LIBS_TO_PROJECT, "false" );
        addProperty( pw, MYECLIPSE_HB_GEN_VERSION_TAG, "false" );
        addProperty( pw, MYECLIPSE_HB_SESSION_FACTORY_ID, (String) hibernateConfig.get( "session-factory-id" ) );
        addProperty( pw, MYECLIPSE_HB_BASE_PERSISTENT_CLASS, null );
        addProperty( pw, MYECLIPSE_HB_RE_SETTINGS_FILE, null );
        addProperty( pw, MYECLIPSE_HB_CONFIG_FILE,
                     makePathToHibernateConfigFile( (String) hibernateConfig.get( "config-file" ) ) );
        addProperty( pw, MYECLIPSE_HB_CREATE_CONFIG_FILE, "false" );
        addProperty( pw, MYECLIPSE_HB_ADD_LIBS_TO_CLASSPATH, "false" );
        addProperty( pw, MYECLIPSE_HB_BASE_DAO_CLASS, null );

        IOUtil.close( w );
    }

    /**
     * Writes a configuration property to the PrintWriter given in parameter
     * 
     * @param pw the PrintWriter to write to
     * @param propName the property name
     * @param propValue the property value (writes empty String if null)
     */
    private void addProperty( PrintWriter pw, String propName, String propValue )
    {
        StringBuilder sb = new StringBuilder( 64 );

        sb.append( propName ).append( "=" );

        if ( propValue != null )
        {
            sb.append( propValue );
        }

        pw.println( sb.toString() );
    }

    /**
     * Find Hibernate version in project dependencies
     * 
     * @return the version of the hibernate artifact if found in the dependencies or 3.2 (default value)
     */
    private String getHibernateVersion()
    {
        String version =
            IdeUtils.getArtifactVersion( new String[] { "hibernate" }, config.getProject().getDependencies(), 3 );

        return version != null ? version : "3.2";
    }

    /**
     * Prepend the project artifactId to the path given in the plugin configuration
     * 
     * @return the path to the file relative to the root of the Eclipse Workspace
     */
    private String makePathToHibernateConfigFile( String configFile )
    {
        StringBuilder sb = new StringBuilder( 64 );

        sb.append( "/" ).append( config.getProject().getArtifactId() ).append( "/" ).append( configFile );

        return sb.toString();
    }
}