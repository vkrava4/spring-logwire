package com.vladkrava.spring.logwire

import com.vladkrava.spring.logwire.annotations.Slf4jWire
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.memberProperties

/**
 * [Slf4jLogPostProcessor] detects properties annotated with [Slf4jWire] to initialise [Logger] post been construction
 *
 * @author Vlad Krava <vkrava4@gmail.com>
 */
@Component
class Slf4jLogPostProcessor : BeanPostProcessor {

    private val log: Logger = LoggerFactory.getLogger(Slf4jLogPostProcessor::class.java)

    /**
     * Applies this [BeanPostProcessor] to the given new bean instance with initialised [Logger] for properties annotated with [Slf4jWire].
     *
     * @param bean the new bean instance
     * @param beanName the name of the bean
     *
     * @return the bean instance to use, either the original or a wrapped one;
     * if `null`, no subsequent BeanPostProcessors will be invoked
     */
    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any =
        bean.also {
            try {
                val loggerName = it::class.java.canonicalName
                setLoggerForProperty(it, loggerName)
                it::class.companionObjectInstance?.let { companion ->
                    setLoggerForProperty(companion, loggerName)
                }
            } catch (ignore: Throwable) {
                // ignore exceptions
            }
        }

    /**
     * Invokes via reflection a property setter for properties annotated with [Slf4jWire]
     */
    private fun setLoggerForProperty(target: Any, loggerName: String) {
        target::class.memberProperties.forEach { property ->
            if (property is KMutableProperty<*>) {
                property.annotations.filterIsInstance(Slf4jWire::class.java)
                    .forEach { _ ->
                        property.setter.call(target, LoggerFactory.getLogger(loggerName))
                        log.debug("Invoked LoggerFactory#getLogger for {}", target)
                    }
            }
        }
    }
}
